package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.food

import com.makaryostudio.lavilo.data.model.Food

interface FoodFragmentItemClickListener {
    fun amountClickListener(food: Food)
}