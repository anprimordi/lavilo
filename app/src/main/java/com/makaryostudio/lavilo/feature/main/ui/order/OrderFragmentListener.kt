package com.makaryostudio.lavilo.feature.main.ui.order

import com.makaryostudio.lavilo.model.Order

interface OrderFragmentListener {
    fun onClickListener(order: Order)
}