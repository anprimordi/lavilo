package com.makaryostudio.lavilo.feature.main.ui.dish.cart

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Cart
import java.text.NumberFormat
import java.util.*

class CartFragmentAdapter(
    val context: Context,
    private val listCart: ArrayList<Cart>,
    private val clickListener: CartFragmentItemClickListener
) :
    RecyclerView.Adapter<CartFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = listCart[position]

        val locale = Locale("in", "ID")

        val formatRupiah = NumberFormat.getNumberInstance(locale)

        val rupiah = formatRupiah.format(cart.price.toDouble())

        holder.textQuantity.text = cart.quantity + "x"
        holder.textName.text = cart.dishName
        holder.textPrice.text = rupiah

        holder.imageDelete.setOnClickListener {
            clickListener.deleteCartItem(cart, position)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textQuantity: TextView = view.findViewById(R.id.text_item_cart_quantity)
        var textName: TextView = view.findViewById(R.id.text_item_cart_name)
        var textPrice: TextView = view.findViewById(R.id.text_item_cart_price)
        var imageDelete: ImageButton = view.findViewById(R.id.button_item_cart_delete)
    }
}