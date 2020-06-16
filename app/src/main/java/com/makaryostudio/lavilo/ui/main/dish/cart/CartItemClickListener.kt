package com.makaryostudio.lavilo.ui.main.dish.cart

import com.makaryostudio.lavilo.data.model.Cart

interface CartItemClickListener {
    fun onDeleteCartItem(cart: Cart, position: Int)
}