package com.makaryostudio.lavilo.feature.management.check.dish.food

import com.makaryostudio.lavilo.model.Food

interface CheckFoodItemClickListener {
    fun onUpdate(food: Food, position: Int)
    fun onDelete(food: Food, position: Int)
}