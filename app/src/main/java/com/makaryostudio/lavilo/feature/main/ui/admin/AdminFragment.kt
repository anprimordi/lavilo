@file:Suppress("DEPRECATION")

package com.makaryostudio.lavilo.feature.main.ui.admin

import android.app.ProgressDialog
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

    private lateinit var adminViewModel: AdminViewModel
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    //    private lateinit var buttonLogin: Button
    private val firebaseAuth = FirebaseAuth.getInstance()
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

//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val editEmail: TextInputEditText = view.findViewById(R.id.edit_email)
        val editPassword: TextInputEditText = view.findViewById(R.id.edit_password)


        authStateListener = FirebaseAuth.AuthStateListener {
            val firebaseUser = firebaseAuth.currentUser
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
                                if (editEmail.text.toString() == "admin@gmail.com") {
                                    databaseReference.removeEventListener(this)

                                    Toast.makeText(
                                        requireContext(),
                                        "Login sukses",
                                        Toast.LENGTH_SHORT
                                    ).show()
//                                    findNavController().navigate(R.id.action_navigation_admin_to_managementFragment)
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
