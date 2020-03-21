package com.makaryostudio.lavilo.data.model

data class Order(
    val id: String,
    val status: String,
    val timestamp: String,
    val bill: String,
    val tableNumber: String,
    val payment: String,
    val change: String
)