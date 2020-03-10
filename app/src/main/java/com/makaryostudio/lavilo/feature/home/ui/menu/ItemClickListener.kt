package com.makaryostudio.lavilo.feature.home.ui.menu

import com.makaryostudio.lavilo.data.model.Food

//TODO use item click listener to food quantity
interface ItemClickListener {
    fun onItemClick(foodQtyHashMap: HashMap<Food, Int>)
}