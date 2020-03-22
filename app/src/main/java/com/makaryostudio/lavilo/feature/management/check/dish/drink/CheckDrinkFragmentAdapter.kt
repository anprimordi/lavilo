package com.makaryostudio.lavilo.feature.management.check.dish.drink

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Drink

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
        var imgbtnEdit: ImageButton = itemView.findViewById(R.id.image_button_item_stock_edit)
        var imgbtnDelete: ImageButton = itemView.findViewById(R.id.image_button_item_stock_delete)
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

        Glide.with(context).load(drink.imageUrl).into(holder.imageThumbnail)
        holder.textName.text = drink.name
        holder.textPrice.text = drink.price
        holder.textStock.text = drink.stock

        holder.imgbtnEdit.setOnClickListener {
            listener.onEdit(drink)
        }

        holder.imgbtnDelete.setOnClickListener {
            listener.onDelete(position)
        }
    }
}