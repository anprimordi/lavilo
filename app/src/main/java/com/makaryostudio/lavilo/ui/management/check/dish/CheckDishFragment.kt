package com.makaryostudio.lavilo.ui.management.check.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.makaryostudio.lavilo.R
import kotlinx.android.synthetic.main.fragment_check_dish.*

class CheckDishFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_dish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragmentAdapter = CheckDishViewPagerAdapter(childFragmentManager)

        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar_check_dish)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.dishFragment, R.id.orderFragment, R.id.adminFragment
            )
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)

        vp_check_dish.adapter = fragmentAdapter
        tab_check_dish.setupWithViewPager(vp_check_dish)
    }
}
