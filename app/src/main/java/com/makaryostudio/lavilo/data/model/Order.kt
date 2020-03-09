package com.makaryostudio.lavilo.data.model

data class Order(
    val idOrder: Int,
    val listOrder: HashMap<Food, Int>,
    val dateOrder: String,
    val tableOrder: Int,
    val totalOrder: Long,
    val paymentOrder: Long,
    val changeOrder: Long
)