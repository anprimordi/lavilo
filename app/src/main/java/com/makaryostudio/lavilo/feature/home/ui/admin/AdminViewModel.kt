package com.makaryostudio.lavilo.feature.home.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminViewModel : ViewModel() {

    val _email = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }

    val _password = MutableLiveData<String>().apply {
        value = ""
    }
    val email: LiveData<String> = _email
    val password: LiveData<String> = _password

    fun doLogin() {
        _email.value
        _password.value
    }
}