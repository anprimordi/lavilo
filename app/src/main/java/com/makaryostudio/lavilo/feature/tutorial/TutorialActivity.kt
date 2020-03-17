package com.makaryostudio.lavilo.feature.tutorial

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import java.util.*

class TutorialActivity : AppCompatActivity() {

    private var quantity = 0
    private lateinit var imageUpload: ImageView
    private lateinit var decreaseQty: ImageView
    private lateinit var increaseQty: ImageView
    private var nameUpload: EditText? = null
    private var priceUpload: EditText? = null
    private lateinit var quantityUpload: EditText
    private lateinit var thresholdUpload: EditText
    private var pathFile: TextView? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var btnUpload: Button
    private var imageUri: Uri? = null
    private var datePickerDialog: DatePickerDialog? = null
    private var storageReference: StorageReference? = null
    private var databaseReference: DatabaseReference? = null
    private var uploadTask: StorageTask<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        imageUpload = findViewById(R.id.image_detail_hidangan)
        decreaseQty = findViewById(R.id.image_decrease)
        increaseQty = findViewById(R.id.image_increase)
        nameUpload = findViewById(R.id.edit_name)
        priceUpload = findViewById(R.id.edit_price)
        quantityUpload = findViewById(R.id.edit_quantity)
        thresholdUpload = findViewById(R.id.edit_threshold)
        pathFile = findViewById(R.id.text_upload_image)
        progressBar = findViewById(R.id.progressbar_tutorial)
        btnUpload = findViewById(R.id.button_upload)
        storageReference = FirebaseStorage.getInstance().getReference("hidangan")
        databaseReference = FirebaseDatabase.getInstance().getReference("hidangan")
        progressBar.visibility = View.GONE
        //        onclick listener event
        imageUpload.setOnClickListener(View.OnClickListener { v: View? -> openFileChooser() })
        decreaseQty.setOnClickListener(View.OnClickListener { v: View? ->
            if (quantity <= 0) {
                quantity = 0
            } else {
                quantity -= 1
            }
            quantityUpload.setText(quantity.toString())
        })
        increaseQty.setOnClickListener {
            quantity += 1
            quantityUpload.setText(quantity.toString())
        }
        btnUpload.setOnClickListener {
            if (uploadTask != null && uploadTask!!.isInProgress) {
                Toast.makeText(this, "upload is in progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadFile()
            }
        }
        thresholdUpload.setOnClickListener(View.OnClickListener { v: View? ->
            val calendar = Calendar.getInstance()
            val day = calendar[Calendar.DAY_OF_MONTH]
            val month = calendar[Calendar.MONTH]
            val year = calendar[Calendar.YEAR]
            datePickerDialog = DatePickerDialog(
                this,
                OnDateSetListener { view: DatePicker?, year1: Int, month1: Int, dayOfMonth: Int ->
                    thresholdUpload.setText(dayOfMonth.toString() + "/" + (month1 + 1) + "/" + year1)
                },
                year,
                month,
                day
            )
            datePickerDialog!!.show()
        })
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

    private fun uploadFile() {
        if (imageUri != null) {
            val fileReference = storageReference
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
                                val model =
                                    Model(
                                        imageUrl,
                                        nameUpload!!.text.toString(),
                                        priceUpload!!.text.toString(),
                                        quantityUpload.text.toString(),
                                        thresholdUpload.text.toString()
                                    )
                                val uid = databaseReference!!.push().key
                                databaseReference!!.child(uid!!).setValue(model)
                            }
                        }
                    }
                    //                        progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "upload successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AnActivity::class.java))
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

    companion object {
        private const val IMAGE_RC = 701
    }
}