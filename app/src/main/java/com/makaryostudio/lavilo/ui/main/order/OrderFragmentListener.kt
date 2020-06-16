package com.makaryostudio.lavilo.ui.main.order

import com.makaryostudio.lavilo.data.model.Order

interface OrderFragmentListener {
    fun onClickListener(order: Order)
}