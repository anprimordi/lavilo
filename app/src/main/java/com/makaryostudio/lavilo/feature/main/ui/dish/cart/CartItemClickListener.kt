package com.makaryostudio.lavilo.feature.main.ui.dish.cart

import com.makaryostudio.lavilo.data.model.Cart

interface CartItemClickListener {
    fun onDeleteCartItem(cart: Cart, position: Int)
}