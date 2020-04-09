package com.makaryostudio.lavilo.feature.main.ui.dish.cart

import com.makaryostudio.lavilo.model.Cart

interface CartItemClickListener {
    fun onDeleteCartItem(cart: Cart, position: Int)
}