package com.makaryostudio.lavilo.feature.main.ui.order.detail

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

class OrderDetailFragmentAdapter(
    private val context: Context,
    private val listOrderDetail: ArrayList<OrderDetail>
) : RecyclerView.Adapter<OrderDetailFragmentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var quantity: TextView = itemView.findViewById(R.id.text_item_order_detail_quantity)
        var name: TextView = itemView.findViewById(R.id.text_item_order_detail_name)
        var price: TextView = itemView.findViewById(R.id.text_item_order_detail_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

        holder.quantity.text = orderDetail.quantity
        holder.name.text = orderDetail.name
        holder.price.text = rupiah
    }
}