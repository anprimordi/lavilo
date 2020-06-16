package com.makaryostudio.lavilo.ui.main.dish.food

import com.makaryostudio.lavilo.data.model.Food

interface FoodItemClickListener {
    fun amountClickListener(food: Food)
}