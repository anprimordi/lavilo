package com.makaryostudio.lavilo.ui.management.check.dish.food

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Food

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
class UpdateFoodFragment : Fragment() {

    private var quantity = 0
    private lateinit var btnUpload: Button
    private lateinit var editName: EditText
    private lateinit var editPrice: EditText
    private lateinit var editStock: EditText
    private lateinit var imageThumbnail: ImageView
    private lateinit var imageDecreaseQty: ImageView
    private lateinit var imageIncreaseQty: ImageView
    private lateinit var textFileDirectory: TextView
    private lateinit var progressBar: ProgressBar
    private var imageUri: Uri? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var uploadTask: StorageTask<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: UpdateFoodFragmentArgs by navArgs()
        val food = args.food

        btnUpload = view.findViewById(R.id.button_update_dish_upload)

        editName = view.findViewById(R.id.edit_update_dish_name)
        editPrice = view.findViewById(R.id.edit_update_dish_price)
        editStock = view.findViewById(R.id.edit_update_dish_stock)

        imageThumbnail = view.findViewById(R.id.image_update_dish_thumbnail)
        imageDecreaseQty = view.findViewById(R.id.image_update_dish_decrease)
        imageIncreaseQty = view.findViewById(R.id.image_update_dish_increase)

        textFileDirectory = view.findViewById(R.id.text_update_dish_directory)
        progressBar = view.findViewById(R.id.progressbar_update_dish)

        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        Glide.with(requireContext())
            .load(food.imageUrl)
            .into(imageThumbnail)

        editName.setText(food.name)
        editPrice.setText(food.price)
        editStock.setText(food.stock)

        quantity = food.stock!!.toInt()

        imageThumbnail.setOnClickListener {
            openFileChooser()
        }

        textFileDirectory.setOnClickListener {
            openFileChooser()
        }

        imageDecreaseQty.setOnClickListener {
            if (quantity != 0) {
                quantity--
            }
            editStock.setText(quantity.toString())
        }

        imageIncreaseQty.setOnClickListener {
            quantity++
            editStock.setText(quantity.toString())
        }

        btnUpload.setOnClickListener {
            var go = true
            if (uploadTask != null && uploadTask!!.isInProgress) {
                Toast.makeText(
                    requireContext(),
                    "upload sedang berjalan",
                    Toast.LENGTH_SHORT
                ).show()
                go = false
            }
            if (editName.text.toString() == "") {
                editName.error = "Nama hidangan nggak boleh kosong ya"
                editName.requestFocus()
                go = false
            }
            if (editPrice.text.toString() == "" || editPrice.text.toString() == "0") {
                editPrice.error = "Hidangan mau digratisin?"
                editPrice.requestFocus()
                go = false
            }
            if (editStock.text.toString() == "" || editStock.text.toString() == "0") {
                editStock.error = "Stok belum diisi"
                editStock.requestFocus()
                go = false
            }
            if (go) uploadFileFood(food)
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RC_IMAGE)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            imageUri = data.data
            val path = data.data!!.path
            textFileDirectory.text = path
            Glide.with(this).load(imageUri).into(imageThumbnail)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(requireContext().contentResolver!!.getType(uri))
    }

    private fun uploadFileFood(foodie: Food) {

        when {
            imageUri != null -> {
                val fileReference = storageReference.child("Food")
                    .child(
                        System.currentTimeMillis().toString() + "." + getFileExtension(
                            imageUri!!
                        )
                    )
                uploadTask = fileReference.putFile(imageUri!!)
                    .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                        val handler = Handler()
                        handler.postDelayed(
                            { progressBar.progress = 0 },
                            5000
                        )
                        if (taskSnapshot.metadata != null) {
                            if (taskSnapshot.metadata!!.reference != null) {
                                val result =
                                    taskSnapshot.storage.downloadUrl
                                result.addOnSuccessListener { uri: Uri ->
                                    val imageUrl = uri.toString()
                                    //createNewPost(imageUrl);
                                    val food =
                                        Food(
                                            imageUrl,
                                            editName.text.toString(),
                                            editPrice.text.toString(),
                                            editStock.text.toString()
                                        )
                                    food.key = foodie.key
                                    databaseReference.child("Dish").child("Food").child(food.key!!)
                                        .setValue(food)
                                }
                            }
                        }
                        Toast.makeText(
                            requireContext(),
                            "Hidangan berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        findNavController().navigate(R.id.action_updateFoodFragment_to_checkDishFragment)

                    }
                    .addOnFailureListener { e: Exception ->
                        Toast.makeText(
                            requireContext(),
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("AddDishFragment", e.message!!)
                    }
                    .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                        //                        progressBar.setVisibility(View.VISIBLE);
                        val progress =
                            100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progressBar.progress = progress.toInt()
                    }
            }
            foodie.imageUrl!!.isNotEmpty() -> {
                val food =
                    Food(
                        foodie.imageUrl,
                        editName.text.toString(),
                        editPrice.text.toString(),
                        editStock.text.toString()
                    )
                // val uid = databaseReference.child("Dish").child("Food").push().key
                food.key = foodie.key
                databaseReference.child("Dish").child("Food").child(food.key!!)
                    .setValue(food).addOnCompleteListener {
                        Toast.makeText(
                            requireContext(),
                            "Hidangan berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        findNavController().navigate(R.id.action_updateFoodFragment_to_checkDishFragment)
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        Log.e("gagal mengupload update food", it.message, it.cause)
                    }

            }
            else -> {
                Toast.makeText(requireContext(), "Tidak ada foto yang dipilih", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val RC_IMAGE = 701
    }
}
