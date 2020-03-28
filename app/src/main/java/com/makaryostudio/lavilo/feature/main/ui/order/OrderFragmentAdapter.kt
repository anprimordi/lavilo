package com.makaryostudio.lavilo.feature.main.ui.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Order
import java.text.NumberFormat
import java.util.*

class OrderFragmentAdapter(
    val context: Context,
    private val listOrder: ArrayList<Order>,
    private val listener: OrderFragmentListener
) : RecyclerView.Adapter<OrderFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_order_list, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = listOrder[position]

        val locale = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(locale)

        val rupiah = formatRupiah.format(order.bill!!.toDouble())

        holder.tableNumber.text = order.tableNumber
        holder.totalBill.text = rupiah

        holder.layout.setOnClickListener {
            listener.onClickListener(order)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: ConstraintLayout = itemView.findViewById(R.id.layout_order_list)
        var tableNumber: TextView = itemView.findViewById(R.id.text_item_order_list_table_number)
        var totalBill: TextView = itemView.findViewById(R.id.text_item_order_list_total_bill)
    }
}