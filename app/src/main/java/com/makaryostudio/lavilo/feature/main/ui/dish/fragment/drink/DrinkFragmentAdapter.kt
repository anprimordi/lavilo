package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.drink

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Drink

class DrinkFragmentAdapter(
    private val context: Context,
    private val listDrink: List<Drink>,
    private val drinkFragmentItemClickListener: DrinkFragmentItemClickListener
) : RecyclerView.Adapter<DrinkFragmentAdapter.ViewHolder>() {

    val quantity = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrinkFragmentAdapter.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDrink.size
    }

    override fun onBindViewHolder(holder: DrinkFragmentAdapter.ViewHolder, position: Int) {
        val drink = listDrink[position]

        Glide.with(context).load(drink.imageUrl).into(holder.image)
        holder.name.text = drink.name
        holder.price.text = drink.price
        holder.stock.text = drink.stock
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image_item_dish_thumbnail)
        val decrease: ImageView = itemView.findViewById(R.id.image_item_dish_decrease)
        val increase: ImageView = itemView.findViewById(R.id.image_item_dish_increase)
        val name: TextView = itemView.findViewById(R.id.text_item_dish_name)
        val price: TextView = itemView.findViewById(R.id.text_item_dish_price)
        val stock: TextView = itemView.findViewById(R.id.text_item_dish_stock)
        val quantity: TextView = itemView.findViewById(R.id.text_item_dish_quantity)
    }
}