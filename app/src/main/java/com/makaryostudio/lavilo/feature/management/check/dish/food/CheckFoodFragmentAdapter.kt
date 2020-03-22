package com.makaryostudio.lavilo.feature.management.check.dish.food

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
import com.makaryostudio.lavilo.data.model.Food

class CheckFoodFragmentAdapter(
    val context: Context,
    val listFood: ArrayList<Food>,
    val listener: CheckFoodItemClickListener
) : RecyclerView.Adapter<CheckFoodFragmentAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageThumbnail: ImageView = itemView.findViewById(R.id.image_item_stock_thumbnail)
        var textName: TextView = itemView.findViewById(R.id.text_item_stock_name)
        var textPrice: TextView = itemView.findViewById(R.id.text_item_stock_price)
        var textStock: TextView = itemView.findViewById(R.id.text_item_stock_quantity)
        var buttonDelete: Button = itemView.findViewById(R.id.button_stock_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_stock, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = listFood[position]

        Glide.with(context).load(food.imageUrl).into(holder.imageThumbnail)
        holder.textName.text = food.name
        holder.textPrice.text = food.price
        holder.textStock.text = food.stock

        holder.buttonDelete.setOnClickListener {
            listener.onDelete(position)
        }
    }
}