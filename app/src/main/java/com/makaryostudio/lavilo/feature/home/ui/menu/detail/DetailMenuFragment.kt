package com.makaryostudio.lavilo.feature.home.ui.menu.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.makaryostudio.lavilo.R

class DetailMenuFragment : Fragment() {

    companion object {
        fun newInstance() = DetailMenuFragment()
    }

    private lateinit var viewModel: DetailMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_menu_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailMenuViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
