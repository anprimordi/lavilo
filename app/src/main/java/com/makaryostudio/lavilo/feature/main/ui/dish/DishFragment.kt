@file:Suppress("DEPRECATION")

package com.makaryostudio.lavilo.feature.main.ui.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.makaryostudio.lavilo.R

class DishFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var exFabCart: ExtendedFloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewPager = view.findViewById(R.id.vp_dish)
        tabLayout = view.findViewById(R.id.tab_dish)
        exFabCart = view.findViewById(R.id.exfab_go_to_cart)

        val fragmentAdapter = DishViewPagerAdapter(childFragmentManager)
        viewPager.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)

        exFabCart.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dish_to_cartFragment)

        }
    }
}
