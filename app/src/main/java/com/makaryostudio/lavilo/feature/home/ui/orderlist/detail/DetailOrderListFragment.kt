package com.makaryostudio.lavilo.feature.home.ui.orderlist.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makaryostudio.lavilo.R

class DetailOrderListFragment : Fragment() {

    companion object {
        fun newInstance() = DetailOrderListFragment()
    }

    private lateinit var viewModel: DetailOrderListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_order_list_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailOrderListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
