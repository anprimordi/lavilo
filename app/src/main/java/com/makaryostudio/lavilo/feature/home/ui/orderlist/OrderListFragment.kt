package com.makaryostudio.lavilo.feature.home.ui.orderlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.makaryostudio.lavilo.R

class OrderListFragment : Fragment() {

    private lateinit var orderListViewModel: OrderListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        orderListViewModel =
            ViewModelProvider(this).get(OrderListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home_order_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        orderListViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}