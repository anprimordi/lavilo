package com.makaryostudio.lavilo.feature.home.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.makaryostudio.lavilo.R

class AdminFragment : Fragment() {

    private lateinit var adminViewModel: AdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminViewModel =
            ViewModelProvider(this).get(AdminViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home_admin, container, false)
        val textView: TextView = root.findViewById(R.id.text_login_admin)
        adminViewModel.email.observe(viewLifecycleOwner, Observer {
            it
        })
        adminViewModel.password.observe(viewLifecycleOwner, Observer {
            it
        })
        val btnLogin: Button = root.findViewById(R.id.button_login)
        btnLogin.setOnClickListener {

            adminViewModel.doLogin()
        }
        return root
    }
}