package com.makaryostudio.lavilo.feature.home.ui.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.model.Food

class MenuAdapter(private val context: Context) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private var mListData: List<Food>? = null

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
        return mListData!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(food: Food) {
            Glide.w
        }
    }
}