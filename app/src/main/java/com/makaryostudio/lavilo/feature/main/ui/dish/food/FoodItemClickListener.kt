package com.makaryostudio.lavilo.feature.main.ui.dish.food

import com.makaryostudio.lavilo.data.model.Food

interface FoodItemClickListener {
    fun amountClickListener(food: Food)
}