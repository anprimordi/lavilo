package com.makaryostudio.lavilo.model

data class Food(
    val idFood: Int,
    val imageFood: String,
    val nameFood: String,
    val priceFood: Long,
    val quantityFood: Int,
    val totalFood: Int,
    val stockFood: Int
)