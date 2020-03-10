package com.makaryostudio.lavilo.feature.home.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Food
import kotlinx.android.synthetic.main.fragment_home_menu.*

class MenuFragment : Fragment() {

    private lateinit var adapter: MenuAdapter
    private lateinit var menuViewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val orderFood = arrayListOf<HashMap<Food, Int>>()

        val quantity = listOf<Int>()

//        val clickListener: ItemClickListener = object : ItemClickListener {
//            override fun onItemClick(
//                food: Food,
//                foodQtyHashMap: HashMap<Food, Int>,
//                imgFood: String,
//                nameFood: String,
//                priceFood: String
//            ) {
//
//                var foodQty = 0
//                var totalPrice = 0
//
//                val dialog = MaterialAlertDialogBuilder(requireContext())
//                val dialogView = layoutInflater.inflate(R.layout.item_quantity, null)
//
//                val textQty: TextView = dialogView.findViewById(R.id.text_item_food_quantity)
//                val btnDecrease: ImageButton =
//                    dialogView.findViewById(R.id.image_item_food_decrease)
//                val btnIncrease: ImageButton =
//                    dialogView.findViewById(R.id.image_item_food_increase)
//
//                val textTotalPrice: TextView =
//                    dialogView.findViewById(R.id.text_item_qty_total_price)
//                val imageFood: ImageView = dialogView.findViewById(R.id.image_item_food)
//                val textNameFood: TextView = dialogView.findViewById(R.id.text_food_name)
//
//                imageFood.setImageResource(imgFood.toInt())
//                textNameFood.text = nameFood
//
//                textQty.text = "$foodQty"
//                textTotalPrice.text = "$totalPrice"
//
//                totalPrice *= foodQty
//
//                dialog.setView(dialogView)
//                dialog.setCancelable(false)
//                dialog.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int -> }
//
//                btnDecrease.setOnClickListener {
//                    if (foodQty < 0) foodQty = 0
//                    foodQty -= 1
////                    totalPrice *= foodQty
////                    return@setOnClickListener
//                }
//
//                btnIncrease.setOnClickListener {
//                    foodQty += 1
////                    totalPrice *= foodQty
////                    textQty.text = "$foodQty"
////                    textTotalPrice.text = "$totalPrice"
////                    return@setOnClickListener
//                }
//
//                val customDialog = dialog.create()
//                customDialog.show()
//                customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
//
//                    foodQtyHashMap[food] = foodQty
//
//                    orderFood.add(foodQtyHashMap)
//
//                    System.out.println(foodQty)
//                    System.out.println(nameFood)
//                }
////                TODO show alert dialog to increase and decrease
//            }
//        }

        menuViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home_menu, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        menuViewModel.getData()
        menuViewModel.listFood.observe(viewLifecycleOwner, Observer {
            it
        })


        adapter = MenuAdapter(requireContext())

//        menuViewModel.fetchData()
        menuViewModel.fetchData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            recyclerview_home_menu.adapter = adapter
            recyclerview_home_menu.layoutManager = LinearLayoutManager(requireContext())
        })



        return root
    }

}