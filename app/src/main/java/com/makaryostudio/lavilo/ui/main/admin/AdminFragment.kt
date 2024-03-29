@file:Suppress("DEPRECATION")

package com.makaryostudio.lavilo.ui.main.admin

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.makaryostudio.lavilo.R
import kotlinx.android.synthetic.main.fragment_admin.*

class AdminFragment : Fragment() {

    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    //    private lateinit var buttonLogin: Button
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("Admin")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

//    fungsi saat view dibuat pada fragment
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    val editEmail: TextInputEditText = view.findViewById(R.id.edit_email)
    val editPassword: TextInputEditText = view.findViewById(R.id.edit_password)

    authStateListener = FirebaseAuth.AuthStateListener {
        val firebaseUser = it.currentUser
        if (firebaseUser != null) {
            findNavController().navigate(R.id.action_navigation_admin_to_managementFragment)
        }
    }

    button_login.setOnClickListener {
        var go = true
        if (editEmail.text.toString() == "") {
            editEmail.error = "email gak boleh kosong ya"
            editEmail.requestFocus()
            go = false
        }
        if (editPassword.text.toString() == "") {
            editPassword.error = "password gak boleh kosong ya"
            editPassword.requestFocus()
            go = false
        }
        if (go) {
            val progressDialog =
                ProgressDialog.show(
                    requireContext(),
                    "Mohon tunggu sebentar",
                    "processing",
                    true
                )

            firebaseAuth.signInWithEmailAndPassword(
                editEmail.text.toString(),
                editPassword.text.toString()
            ).addOnCompleteListener {
                progressDialog.dismiss()
                if (it.isSuccessful) {

                    databaseReference.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            if (editEmail.text.toString() == "admin@gmail.com") {
                                databaseReference.removeEventListener(this)

                                Toast.makeText(
                                    requireContext(),
                                    "Login sukses",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e(
                                "ERROR",
                                databaseError.message,
                                databaseError.toException()
                            )
                        }
                    })
                } else {
                    Log.e("ERROR", it.exception.toString())
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

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}
