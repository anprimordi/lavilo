package com.makaryostudio.lavilo.feature.main.ui.dish.cart

import com.makaryostudio.lavilo.data.model.Cart

interface CartFragmentItemClickListener {
    fun deleteCartItem(cart: Cart, position: Int)
}