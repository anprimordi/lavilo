package com.makaryostudio.lavilo.feature.main.ui.admin

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.feature.tutorial.TutorialActivity
import kotlinx.android.synthetic.main.fragment_admin.*

class AdminFragment : Fragment() {

    private lateinit var adminViewModel: AdminViewModel


    //    private lateinit var buttonLogin: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("Admin")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminViewModel =
            ViewModelProviders.of(this).get(AdminViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_admin, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        adminViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val editEmail: TextInputEditText = view.findViewById(R.id.edit_email)
//        val editPassword: TextInputEditText = view.findViewById(R.id.edit_password)
        firebaseAuth = FirebaseAuth.getInstance()

        button_login.setOnClickListener {
            var go = true
            if (edit_email.text.toString() == "") {
                edit_email.error = "email gak boleh kosong ya"
                edit_email.requestFocus()
                go = false
            }
            if (edit_password.text.toString() == "") {
                edit_password.error = "password gak boleh kosong ya"
                edit_email.requestFocus()
                go = false
            }
            if (go) {
                val progressDialog =
                    ProgressDialog.show(
                        requireContext(),
                        "please wait",
                        "processing",
                        true
                    )

                firebaseAuth.signInWithEmailAndPassword(
                    edit_email.text.toString(),
                    edit_password.text.toString()
                )
                    .addOnCompleteListener {
                        progressDialog.dismiss()
                        if (it.isSuccessful) {

                            databaseReference.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
//                                    val admin: Admin = dataSnapshot.value as Admin
//                                    if (admin != null) {
//                                        if (admin.type == "Hall Manager" && editEmail.text.toString() == admin.email) {
//                                            databaseReference.removeEventListener(this)
//                                            val intent = Intent(
//                                                requireContext(),
//                                                TutorialActivity::class.java
//                                            )
//                                            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                                            Toast.makeText(
//                                                requireContext(),
//                                                "You are " + admin.type.toString(),
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                            startActivity(intent)
//                                        }
//                                        if (admin.type == "Head Chef" && editEmail.text.toString() == admin.email) {
//                                            databaseReference.removeEventListener(this)
//                                            val intent = Intent(
//                                                requireContext(),
//                                                TutorialActivity::class.java
//                                            )
//                                            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
//                                            Toast.makeText(
//                                                requireContext(),
//                                                "You are " + admin.type.toString(),
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                            startActivity(intent)
//                                        }
//
//                                    }
                                    if (edit_email.text.toString() == "admin@gmail.com") {
                                        databaseReference.removeEventListener(this)
                                        val intent = Intent(
                                            requireContext(),
                                            TutorialActivity::class.java
                                        )
                                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                        Toast.makeText(
                                            requireContext(),
                                            "You are " + edit_email.text.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(intent)
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.w(TAG, "canceled", databaseError.toException())
                                }
                            })
                        } else {
                            Log.e("Error", it.exception.toString())
                            Toast.makeText(
                                requireContext(),
                                it.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

        }
    }
}