package com.makaryostudio.lavilo.feature.home.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.makaryostudio.lavilo.R
import kotlinx.android.synthetic.main.fragment_home_menu.*

class MenuFragment : Fragment() {

    private lateinit var adapter: MenuAdapter
    private lateinit var menuViewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val quantity = listOf<Int>()

        adapter = MenuAdapter(requireContext()) {
            //  TODO how to increase and decrease quantity?
        }

        recyclerview_home_menu.layoutManager = LinearLayoutManager(requireContext())
        recyclerview_home_menu.adapter = adapter

        menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home_menu, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
        menuViewModel.getData()
        menuViewModel.listFood.observe(viewLifecycleOwner, Observer {
            it
        })

        menuViewModel.fetchData()
        menuViewModel.fetchData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
        })

        return root
    }

}