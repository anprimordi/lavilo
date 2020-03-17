package com.makaryostudio.lavilo.feature.main.ui.dish

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.makaryostudio.lavilo.feature.main.ui.dish.fragment.drink.DrinkFragment
import com.makaryostudio.lavilo.feature.main.ui.dish.fragment.food.FoodFragment

class DishViewPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {

        return when (position) {
            0 -> {
                FoodFragment()
            }
            else -> {
                return DrinkFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Makanan"
            else -> {
                return "Minuman"
            }
        }
    }
}