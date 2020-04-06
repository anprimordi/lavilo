package com.makaryostudio.lavilo.feature.main.ui.dish.drink

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Drink
import java.text.NumberFormat
import java.util.*

class DrinkFragmentAdapter(
    private val context: Context,
    private val listDrink: List<Drink>,
    private val clickListener: DrinkFragmentItemClickListener
) : RecyclerView.Adapter<DrinkFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDrink.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val drink = listDrink[position]

        val locale = Locale("in", "ID")

        val formatRupiah = NumberFormat.getCurrencyInstance(locale)

        val rupiah = formatRupiah.format(drink.price!!.toDouble())

        Glide.with(context).load(drink.imageUrl).into(holder.image)
        holder.name.text = drink.name
        holder.price.text = rupiah
        holder.stock.text = drink.stock

        holder.button.setOnClickListener {
            clickListener.amountClickListener(drink)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: ConstraintLayout = itemView.findViewById(R.id.layout_dish_item)
        val image: ImageView = itemView.findViewById(R.id.image_item_dish_thumbnail)
        val name: TextView = itemView.findViewById(R.id.text_item_dish_name)
        val price: TextView = itemView.findViewById(R.id.text_item_dish_price)
        val stock: TextView = itemView.findViewById(R.id.text_item_dish_stock)
        val button: Button = itemView.findViewById(R.id.button_item_dish_add)
    }
}