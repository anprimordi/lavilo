package com.makaryostudio.lavilo.model

data class Order(
    val idOrder: Int,
    val listOrder: HashMap<Food, Int>,
    val dateOrder: String,
    val tableOrder: Int,
    val totalOrder: Long,
    val paymentOrder: Long,
    val changeOrder: Long
)