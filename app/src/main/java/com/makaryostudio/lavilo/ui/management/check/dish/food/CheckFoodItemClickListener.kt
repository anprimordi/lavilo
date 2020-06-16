package com.makaryostudio.lavilo.ui.management.check.dish.food

import com.makaryostudio.lavilo.data.model.Food

interface CheckFoodItemClickListener {
    fun onUpdate(food: Food, position: Int)
    fun onDelete(food: Food, position: Int)
}