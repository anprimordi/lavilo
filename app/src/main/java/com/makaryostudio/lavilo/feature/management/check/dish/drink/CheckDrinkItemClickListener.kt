package com.makaryostudio.lavilo.feature.management.check.dish.drink

import com.makaryostudio.lavilo.data.model.Drink

interface CheckDrinkItemClickListener {
    fun onEdit(drink: Drink)
    fun onDelete(position: Int)
}