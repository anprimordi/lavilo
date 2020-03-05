package com.makaryostudio.lavilo.feature.home.ui.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.model.Food
import kotlinx.android.synthetic.main.item_food.view.*

class MenuAdapter(
    private val context: Context,
    private val onBtnIncreaseClick: (food: Food) -> Unit,
    private val onBtnDecreaseClick: (food: Food) -> Unit
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private var mListData: List<Food> = emptyList()

    internal fun setListData(mListData: List<Food>) {
        this.mListData = mListData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = mListData[position]
        val quantity = 0
        //TODO decreases stock when quantity increases?

        holder.bindView(food)
        holder.increaseQuantity.setOnClickListener {

            quantity + 1
            holder.quantity.text = quantity.toString()
//            holder.stock.text =
            onBtnIncreaseClick(food)
        }

        holder.decreaseQuantity.setOnClickListener {

            if (quantity <= 0) {
                holder.quantity.text = 0.toString()
            } else {
                quantity - 1
                holder.quantity.text = quantity.toString()
            }
            onBtnDecreaseClick(food)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        var layoutItem: ConstraintLayout = itemView.findViewById(R.id.layout_food_item)
        var increaseQuantity: ImageButton = itemView.findViewById(R.id.image_item_food_increase)
        var decreaseQuantity: ImageButton = itemView.findViewById(R.id.image_item_food_decrease)
        var quantity: TextView = itemView.findViewById(R.id.text_item_food_quantity)
        var stock: TextView = itemView.findViewById(R.id.text_item_food_stock)

        fun bindView(food: Food) {
            Glide.with(context).load(food.imageFood).into(itemView.image_item_food_thumbnail)
            itemView.title_item_food_name.text = food.nameFood
            itemView.text_item_food_price.text = food.priceFood.toString()
            itemView.text_item_food_stock.text = food.stockFood.toString()
//            itemView.text_item_food_quantity.text = food.quantityFood.toString()
        }
    }
}