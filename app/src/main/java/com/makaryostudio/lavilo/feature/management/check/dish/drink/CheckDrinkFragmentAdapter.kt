package com.makaryostudio.lavilo.feature.management.check.dish.drink

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Drink
import java.text.NumberFormat
import java.util.*

class CheckDrinkFragmentAdapter(
    val context: Context,
    val listDrink: ArrayList<Drink>,
    val listener: CheckDrinkItemClickListener
) : RecyclerView.Adapter<CheckDrinkFragmentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageThumbnail: ImageView = itemView.findViewById(R.id.image_item_stock_thumbnail)
        var textName: TextView = itemView.findViewById(R.id.text_item_stock_name)
        var textPrice: TextView = itemView.findViewById(R.id.text_item_stock_price)
        var textStock: TextView = itemView.findViewById(R.id.text_item_stock_quantity)
        var buttonUpdate: Button = itemView.findViewById(R.id.button_item_stock_update)
        var buttonDelete: Button = itemView.findViewById(R.id.button_item_stock_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_stock, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDrink.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drink = listDrink[position]

        val locale = Locale("in", "ID")

        val formatRupiah = NumberFormat.getCurrencyInstance(locale)

        val rupiah = formatRupiah.format(drink.price.toDouble())

        Glide.with(context).load(drink.imageUrl).into(holder.imageThumbnail)
        holder.textName.text = drink.name
        holder.textPrice.text = rupiah
        holder.textStock.text = drink.stock

        holder.buttonUpdate.setOnClickListener {
            listener.onUpdate(position)
        }

        holder.buttonDelete.setOnClickListener {
            listener.onDelete(position)
        }
    }
}