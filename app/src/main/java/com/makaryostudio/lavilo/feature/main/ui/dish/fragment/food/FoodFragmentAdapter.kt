package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.food

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

class FoodFragmentAdapter(
    private val context: Context,
    private var listData: List<Food>,
    private val clickListener: FoodFragmentItemClickListener
) : RecyclerView.Adapter<FoodFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = listData[position]

        Glide.with(context).load(food.imageUrl).into(holder.image)
        holder.name.text = food.name
        holder.price.text = food.price
        holder.stock.text = food.stock

        holder.button.setOnClickListener {
            clickListener.amountClickListener(food)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image_item_dish_thumbnail)
        val name: TextView = itemView.findViewById(R.id.text_item_dish_name)
        val price: TextView = itemView.findViewById(R.id.text_item_dish_price)
        val stock: TextView = itemView.findViewById(R.id.text_item_dish_stock)
        val quantity: TextView = itemView.findViewById(R.id.text_item_dish_quantity)
        val button: Button = itemView.findViewById(R.id.button_item_dish_add)
    }
}