package com.makaryostudio.lavilo.feature.main.ui.order

import com.makaryostudio.lavilo.data.model.Order

interface OrderFragmentListener {
    fun onClickListener(order: Order)
}