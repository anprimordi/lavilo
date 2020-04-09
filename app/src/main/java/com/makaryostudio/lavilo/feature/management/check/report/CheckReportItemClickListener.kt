package com.makaryostudio.lavilo.feature.management.check.report

import com.makaryostudio.lavilo.model.Order

interface CheckReportItemClickListener {
    fun onItemClick(order: Order)
    fun onDelete(position: Int)
}