package com.makaryostudio.lavilo.feature.management.add

import android.app.Activity.RESULT_OK
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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Drink
import com.makaryostudio.lavilo.data.model.Food

class AddDishFragment : Fragment() {

    private var quantity = 0
    private lateinit var btnUpload: Button
    private lateinit var editName: EditText
    private lateinit var editPrice: EditText
    private lateinit var editStock: EditText
    private lateinit var imageThumbnail: ImageView
    private lateinit var imageDecreaseQty: ImageView
    private lateinit var imageIncreaseQty: ImageView
    private lateinit var rgType: RadioGroup
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
        return inflater.inflate(R.layout.fragment_add_dish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnUpload = view.findViewById(R.id.button_add_dish_upload)

        editName = view.findViewById(R.id.edit_add_dish_name)
        editPrice = view.findViewById(R.id.edit_add_dish_price)
        editStock = view.findViewById(R.id.edit_add_dish_stock)

        imageThumbnail = view.findViewById(R.id.image_add_dish_thumbnail)
        imageDecreaseQty = view.findViewById(R.id.image_add_dish_decrease)
        imageIncreaseQty = view.findViewById(R.id.image_add_dish_increase)

        rgType = view.findViewById(R.id.rg_add_dish_type)

        textFileDirectory = view.findViewById(R.id.text_add_dish_directory)
        progressBar = view.findViewById(R.id.progressbar_add_dish)

        databaseReference = FirebaseDatabase.getInstance().reference
        storageReference = FirebaseStorage.getInstance().reference

        imageThumbnail.setOnClickListener {
            openFileChooser()
        }

        textFileDirectory.setOnClickListener {
            openFileChooser()
        }

        imageDecreaseQty.setOnClickListener {
            if (quantity != 0) {
                quantity--
                editStock.setText(quantity.toString())
            }
        }

        imageIncreaseQty.setOnClickListener {
            quantity++
            editStock.setText(quantity.toString())
        }

        rgType.setOnCheckedChangeListener { _, checkedId ->
            var go = true
            when (checkedId) {
                R.id.radio_add_dish_food -> {
                    btnUpload.setOnClickListener {
                        if (uploadTask != null && uploadTask!!.isInProgress) {
                            Toast.makeText(
                                requireContext(),
                                "upload sedang berjalan",
                                Toast.LENGTH_SHORT
                            ).show()
                            go = false
                        }
                        if (textFileDirectory.text == "Upload gambar") {
                            Toast.makeText(
                                requireContext(),
                                "Gambarnya belum diupload",
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
                        if (go) uploadFileFood()
                    }
                }
                R.id.radio_add_dish_drink -> {
                    btnUpload.setOnClickListener {
                        if (uploadTask != null && uploadTask!!.isInProgress) {
                            Toast.makeText(
                                requireContext(),
                                "upload sedang berjalan",
                                Toast.LENGTH_SHORT
                            ).show()
                            go = false
                        }
                        if (textFileDirectory.text == "Upload gambar") {
                            Toast.makeText(
                                requireContext(),
                                "Gambarnya belum diupload",
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
                            editPrice.error = "Hidangan mau digratisin ya?"
                            editPrice.requestFocus()
                            go = false
                        }
                        if (editStock.text.toString() == "" || editStock.text.toString() == "0") {
                            editStock.error = "Stok belum diisi"
                            editStock.requestFocus()
                            go = false
                        }
                        if (go) uploadFileDrink()
                    }
                }
                -1 -> {
                    Toast.makeText(
                        requireContext(),
                        "Pilih jenis hidangan",
                        Toast.LENGTH_SHORT
                    ).show()

                    rgType.requestFocus()
                }
            }
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
        if (requestCode == RC_IMAGE && resultCode == RESULT_OK && data != null && data.data != null
        ) {
            imageUri = data.data
            val path = data.data!!.path
            textFileDirectory.text = path
            Glide.with(this).load(imageUri).into(imageThumbnail)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
//        val contentResolver = context?.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(requireContext().contentResolver!!.getType(uri))
    }

    private fun uploadFileFood() {
        if (imageUri != null) {
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
                                val uid = databaseReference.child("Dish").child("Food").push().key
                                food.key = uid
                                databaseReference.child("Dish").child("Food").child(uid!!)
                                    .setValue(food)
                            }
                        }
                    }
                    //                        progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "upload successful", Toast.LENGTH_SHORT).show()
                    //                    startActivity(Intent(this, MainActivity::class.java))
                    findNavController().navigate(R.id.action_addDishFragment_to_managementFragment)

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
        } else {
            Toast.makeText(requireContext(), "Tidak ada foto yang dipilih", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun uploadFileDrink() {
        if (imageUri != null) {
            val fileReference = storageReference.child("Drink")
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

                                val drink =
                                    Drink(
                                        imageUrl,
                                        editName.text.toString(),
                                        editPrice.text.toString(),
                                        editStock.text.toString()
                                    )

                                val uid = databaseReference.child("Dish").child("Drink").push().key
                                drink.key = uid
                                databaseReference.child("Dish").child("Drink").child(uid!!)
                                    .setValue(drink)
                            }
                        }
                    }
                    //                        progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "upload successful", Toast.LENGTH_SHORT).show()
//                    startActivity(Intent(this, MainActivity::class.java))
                    findNavController().navigate(R.id.action_addDishFragment_to_managementFragment)
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
        } else {
            Toast.makeText(
                requireContext(),
                "Tidak ada file yang dipilih",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private const val RC_IMAGE = 701
    }
}
