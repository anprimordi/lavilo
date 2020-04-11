package com.makaryostudio.lavilo.feature.management.check.report.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.OrderDetail
import java.text.NumberFormat
import java.util.*

class CheckReportDetailAdapter(
    private val context: Context,
    private val listOrderDetail: ArrayList<OrderDetail>
) : RecyclerView.Adapter<CheckReportDetailAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textQuantity: TextView = itemView.findViewById(R.id.text_item_order_detail_quantity)
        var textName: TextView = itemView.findViewById(R.id.text_item_order_detail_name)
        var textPrice: TextView = itemView.findViewById(R.id.text_item_order_detail_price)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrderDetail.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderDetail = listOrderDetail[position]

        val locale = Locale("in", "ID")

        val formatRupiah = NumberFormat.getNumberInstance(locale)

        val rupiah = formatRupiah.format(orderDetail.totalPrice.toDouble())

        holder.textQuantity.text = orderDetail.quantity
        holder.textName.text = orderDetail.name
        holder.textPrice.text = rupiah
    }
}