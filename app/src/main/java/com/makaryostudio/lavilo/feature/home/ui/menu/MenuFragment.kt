package com.makaryostudio.lavilo.feature.home.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.makaryostudio.lavilo.R

class MenuFragment : Fragment() {

    private lateinit var menuViewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home_menu, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        menuViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}