package com.makaryostudio.lavilo.ui.management.check.report

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Order
import java.text.NumberFormat
import java.util.*

class CheckReportAdapter(
    private val context: Context,
    private val listOrder: ArrayList<Order>,
    private val clickListener: CheckReportItemClickListener
) : RecyclerView.Adapter<CheckReportAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layoutReport: ConstraintLayout = itemView.findViewById(R.id.layout_item_report)
        var textId: TextView = itemView.findViewById(R.id.text_item_report_id)
        var textTimeStamp: TextView = itemView.findViewById(R.id.text_item_report_timestamp)
        var textBill: TextView = itemView.findViewById(R.id.text_item_report_bill)
        var textTableNumber: TextView = itemView.findViewById(R.id.text_item_report_table_number)
        var textStatus: TextView = itemView.findViewById(R.id.text_item_report_status)
        var buttonDelete: Button = itemView.findViewById(R.id.button_item_report_delete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false)

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

        holder.textId.text = order.id
        holder.textTimeStamp.text = order.timestamp
        holder.textBill.text = rupiah
        holder.textTableNumber.text = order.tableNumber
        holder.textStatus.text = order.status

        if (order.status == "Lunas") {
            holder.textStatus.setTextColor(Color.rgb(165, 217, 213))
        }

        holder.layoutReport.setOnClickListener {
            clickListener.onItemClick(order)
        }

        holder.buttonDelete.setOnClickListener {
            clickListener.onDelete(position)
        }
    }
}