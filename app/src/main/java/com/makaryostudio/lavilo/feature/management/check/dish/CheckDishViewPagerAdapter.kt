package com.makaryostudio.lavilo.feature.management.check.dish

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.makaryostudio.lavilo.feature.management.check.dish.drink.CheckDrinkFragment
import com.makaryostudio.lavilo.feature.management.check.dish.food.CheckFoodFragment

class CheckDishViewPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CheckFoodFragment()
            }
            else -> {
                return CheckDrinkFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Makanan"
            else -> {
                return "Minuman"
            }
        }
    }
}