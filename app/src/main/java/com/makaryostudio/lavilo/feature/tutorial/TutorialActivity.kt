package com.makaryostudio.lavilo.feature.tutorial

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Food
import com.makaryostudio.lavilo.feature.main.MainActivity


class TutorialActivity : AppCompatActivity() {

    private var quantity = 0
    private lateinit var imageUpload: ImageView
    private lateinit var decreaseQty: ImageView
    private lateinit var increaseQty: ImageView
    private var nameUpload: EditText? = null
    private var priceUpload: EditText? = null
    private lateinit var stockUpload: EditText
    private lateinit var rgType: RadioGroup
    private var pathFile: TextView? = null
    private lateinit var textGotoMain: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnUpload: Button
    private var imageUri: Uri? = null
    private var storageReferenceFood: StorageReference? = null
    private lateinit var storageReferenceDrink: StorageReference
    private lateinit var databaseReferenceDrink: DatabaseReference
    private var databaseReferenceFood: DatabaseReference? = null
    private var uploadTask: StorageTask<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        imageUpload = findViewById(R.id.image_detail_hidangan)
        decreaseQty = findViewById(R.id.image_decrease)
        increaseQty = findViewById(R.id.image_increase)
        nameUpload = findViewById(R.id.edit_name)
        priceUpload = findViewById(R.id.edit_price)
        stockUpload = findViewById(R.id.edit_quantity)
        pathFile = findViewById(R.id.text_upload_image)
        progressBar = findViewById(R.id.progressbar_tutorial)
        btnUpload = findViewById(R.id.button_upload)
        rgType = findViewById(R.id.rg_type)

        textGotoMain = findViewById(R.id.text_gotomain)

        textGotoMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        storageReferenceFood = FirebaseStorage.getInstance().getReference("food")
        storageReferenceDrink = FirebaseStorage.getInstance().getReference("drink")
        databaseReferenceFood = FirebaseDatabase.getInstance().getReference("food")
        databaseReferenceDrink = FirebaseDatabase.getInstance().getReference("drink")
        progressBar.visibility = View.GONE
        //        onclick listener event
        imageUpload.setOnClickListener {
            openFileChooser()
        }
        decreaseQty.setOnClickListener {
            if (quantity <= 0) {
                quantity = 0
            } else {
                quantity -= 1
            }
            stockUpload.setText(quantity.toString())
        }
        increaseQty.setOnClickListener {
            quantity += 1
            stockUpload.setText(quantity.toString())
        }

        rgType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_food -> {
                    btnUpload.setOnClickListener {
                        if (uploadTask != null && uploadTask!!.isInProgress)
                            Toast.makeText(
                                this,
                                "upload is in progress",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            uploadFileFood()
                    }
                }
                R.id.radio_drink -> {
                    btnUpload.setOnClickListener {
                        if (uploadTask != null && uploadTask!!.isInProgress)
                            Toast.makeText(
                                this,
                                "upload is in progress",
                                Toast.LENGTH_SHORT
                            ).show()
                        else
                            uploadFileDrink()
                    }
                }
                -1 -> {
                    Toast.makeText(this, "pilih jenis hidangan", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    //    buka file manager
    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_RC)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_RC && resultCode == Activity.RESULT_OK && data != null && data.data != null
        ) {
            imageUri = data.data
            val path = data.data!!.path
            pathFile!!.text = path
            Glide.with(this).load(imageUri).into(imageUpload)
        }
    }

    //    upload gambar
    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadFileFood() {
        if (imageUri != null) {
            val fileReference = storageReferenceFood
                ?.child(
                    System.currentTimeMillis().toString() + "." + getFileExtension(
                        imageUri!!
                    )
                )
            uploadTask = fileReference?.putFile(imageUri!!)
                ?.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
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
                                        nameUpload!!.text.toString(),
                                        priceUpload!!.text.toString(),
                                        stockUpload.text.toString()
                                    )
                                val uid = databaseReferenceFood!!.push().key
                                databaseReferenceFood!!.child(uid!!).setValue(food)
                            }
                        }
                    }
                    //                        progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "upload successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                ?.addOnFailureListener { e: Exception ->
                    Toast.makeText(
                        this,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ?.addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    //                        progressBar.setVisibility(View.VISIBLE);
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressBar.progress = progress.toInt()
                }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadFileDrink() {
        if (imageUri != null) {
            val fileReference = storageReferenceDrink
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
                                        nameUpload!!.text.toString(),
                                        priceUpload!!.text.toString(),
                                        stockUpload.text.toString()
                                    )
                                val uid = databaseReferenceDrink.push().key
                                databaseReferenceDrink.child(uid!!).setValue(food)
                            }
                        }
                    }
                    //                        progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "upload successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                .addOnFailureListener { e: Exception ->
                    Toast.makeText(
                        this,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    //                        progressBar.setVisibility(View.VISIBLE);
                    val progress =
                        100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    progressBar.progress = progress.toInt()
                }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val IMAGE_RC = 701
    }
}