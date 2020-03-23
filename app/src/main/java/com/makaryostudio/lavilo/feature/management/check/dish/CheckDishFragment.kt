package com.makaryostudio.lavilo.feature.management.check.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makaryostudio.lavilo.R
import kotlinx.android.synthetic.main.fragment_check_dish.*

/**
 * A simple [Fragment] subclass.
 */
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

        val fragmentAdapter = CheckDishViewPagerAdapter(childFragmentManager)

        vp_check_dish.adapter = fragmentAdapter
        tab_check_dish.setupWithViewPager(vp_check_dish)
    }
}
