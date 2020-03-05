package com.makaryostudio.lavilo.feature.home.ui.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.model.Food
import kotlinx.android.synthetic.main.item_food.view.*

class MenuAdapter(private val context: Context) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

//    private var mListData: List<Food>? = null
//
//    internal fun setListData(mListData: List<Food>) {
//        this.mListData = mListData
//        notifyDataSetChanged()
//    }

    private var mListData = mutableListOf<Food>()

    internal fun setListData(mListData: MutableList<Food>) {
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
        holder.bindView(food)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(food: Food) {
            Glide.with(context).load(food.imageFood).into(itemView.image_item_food_thumbnail)
            itemView.title_item_food_name.text = food.nameFood
            itemView.text_item_food_price.text = food.priceFood.toString()
            itemView.text_item_food_quantity.text = food.quantityFood.toString()
            itemView.text_item_food_stock.text = food.stockFood.toString()
        }
    }
}