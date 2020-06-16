package com.makaryostudio.lavilo.ui.main.dish

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.makaryostudio.lavilo.ui.main.dish.drink.DrinkFragment
import com.makaryostudio.lavilo.ui.main.dish.food.FoodFragment

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