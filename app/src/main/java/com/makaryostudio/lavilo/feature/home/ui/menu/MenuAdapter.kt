package com.makaryostudio.lavilo.feature.home.ui.menu

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Food
import kotlinx.android.synthetic.main.item_dish.view.*

class MenuAdapter(
    private val context: Context,
    private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private var mListData: List<Food> = emptyList()

    private lateinit var dialog: Dialog
    //    TODO hash data to parse food
    private lateinit var mHashMapFoodQty: HashMap<Food, Int>

    internal fun setListData(mListData: List<Food>) {
        this.mListData = mListData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dish, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = mListData[position]
        //TODO decreases stock when quantity increases?


        holder.bindView(food)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var layoutItem: ConstraintLayout = itemView.findViewById(R.id.layout_food_item)
        var increaseQuantity: ImageButton = itemView.findViewById(R.id.image_item_dish_increase)
        var decreaseQuantity: ImageButton = itemView.findViewById(R.id.image_item_dish_decrease)
        var quantity: TextView = itemView.findViewById(R.id.text_item_dish_quantity)
        var stock: TextView = itemView.findViewById(R.id.text_item_dish_stock)

        fun bindView(food: Food) {
            Glide.with(context).load(food.imageUrl).into(itemView.image_item_dish_thumbnail)
            itemView.text_item_dish_name.text = food.name
            itemView.text_item_dish_price.text = food.price.toString()
            itemView.text_item_dish_stock.text = food.stock.toString()
//            itemView.text_item_food_quantity.text = food.quantityFood.toString()
        }
    }
}