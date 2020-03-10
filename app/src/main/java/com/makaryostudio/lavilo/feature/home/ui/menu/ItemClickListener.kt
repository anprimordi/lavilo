package com.makaryostudio.lavilo.feature.home.ui.menu

import com.makaryostudio.lavilo.data.model.Food

//TODO use item click listener to adjust food quantity
interface ItemClickListener {
    fun onItemClick(
        food: Food,
        foodQtyHashMap: HashMap<Food, Int>,
        imgFood: String,
        nameFood: String,
        priceFood: String
    )
}