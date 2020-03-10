package com.makaryostudio.lavilo.feature.home.ui.admin

import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.makaryostudio.lavilo.data.model.Admin

class AdminViewModel : ViewModel() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var admin: Admin

    val _email = MutableLiveData<String>().apply {
        value = ""
    }

    val _password = MutableLiveData<String>().apply {
        value = ""
    }
    val email: LiveData<String> = _email
    val password: LiveData<String> = _password


    fun doLogin() {
        _email.value
        _password.value

        mAuth.signInWithEmailAndPassword(_email.toString(), _password.toString())
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    Log.d(TAG, "sign in with email success")
                    val user = mAuth.currentUser

//                    Toast.makeText(, "Success",Toast.LENGTH_SHORT).show()
                    System.out.println("login successful")
//                    TODO change successful sign in
                } else {
                    Log.d(TAG, "sign in failed")

                    System.out.println("login failed")
//                    TODO change failed sign in
                }
            }
    }

}