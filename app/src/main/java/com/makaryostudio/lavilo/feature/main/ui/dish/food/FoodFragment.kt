package com.makaryostudio.lavilo.feature.main.ui.dish.food

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Cart
import com.makaryostudio.lavilo.data.model.Food
import kotlinx.android.synthetic.main.fragment_food.*
import java.text.NumberFormat
import java.util.*

class FoodFragment : Fragment() {

    private lateinit var foodAdapter: FoodFragmentAdapter
    private lateinit var rvFood: RecyclerView
    private lateinit var listFood: ArrayList<Food>
    private lateinit var foodFragmentItemClickListener: FoodFragmentItemClickListener
    private lateinit var dbReference: DatabaseReference
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listFood = ArrayList()

        rvFood = view.findViewById(R.id.rv_food)
        progressBar = view.findViewById(R.id.progress_food)

        foodFragmentItemClickListener = object : FoodFragmentItemClickListener {
            override fun amountClickListener(food: Food) {
                showDialog(food)
            }
        }

        foodAdapter = FoodFragmentAdapter(requireContext(), listFood, foodFragmentItemClickListener)

        rvFood.layoutManager = LinearLayoutManager(requireContext())
        rv_food.adapter = foodAdapter

        dbReference = FirebaseDatabase.getInstance().reference

        dbReference.child("Dish").child("Food").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listFood.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val food =
                        postSnapshot.getValue(
                            Food::class.java
                        )!!
                    food.key = postSnapshot.key
                    listFood.add(food)
                }
                foodAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    @SuppressLint("InflateParams")
    private fun showDialog(food: Food) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Tambahin makanan")

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_dish, null)

        var quantityInt = 1
        val priceInt = food.price!!.toInt()

        var totalPrice = priceInt

        val locale = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(locale)

        val textDishName: TextView = view.findViewById(R.id.text_dish_dialog_name)
        val textQuantity: TextView = view.findViewById(R.id.text_dish_dialog_quantity)
        val textPrice: TextView = view.findViewById(R.id.text_dish_dialog_price)
        val buttonDecrease: ImageButton = view.findViewById(R.id.image_dish_dialog_decrease)
        val buttonIncrease: ImageButton = view.findViewById(R.id.image_dish_dialog_increase)

        val rupiah = formatRupiah.format(priceInt.toDouble())

        textPrice.text = rupiah

        textDishName.text = food.name

        dbReference.child("Cart").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {

                    val dummy: Cart = childSnapshot.getValue(Cart::class.java)!!

                    if (food.name == dummy.dishName) {
                        if (dummy.quantity.toInt() != 0) {
                            quantityInt = dummy.quantity.toInt()
                            totalPrice = dummy.price.toInt()

                            val rupiahPrice = formatRupiah.format(totalPrice.toDouble())
                            textQuantity.text = quantityInt.toString()
                            textPrice.text = rupiahPrice
                        }
                    }
                }
            }
        })

        buttonDecrease.setOnClickListener {
            if (quantityInt != 0) {
                quantityInt--
                totalPrice -= priceInt
                val rupiahPrice = formatRupiah.format(totalPrice.toDouble())
                textQuantity.text = quantityInt.toString()
                textPrice.text = rupiahPrice
            }
        }

        buttonIncrease.setOnClickListener {
            quantityInt++
            totalPrice += priceInt
            val rupiahPrice = formatRupiah.format(totalPrice.toDouble())
            textQuantity.text = quantityInt.toString()
            textPrice.text = rupiahPrice
        }

        builder.setView(view)

        builder.setPositiveButton("TAMBAH") { _, _ ->

            val dishName = textDishName.text.toString().trim()

            val quantity = textQuantity.text.toString().trim()

            val key = food.key

            if (quantity.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Pesanannya nggak boleh kosong ya",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setPositiveButton
            }

            val cart = Cart(key!!, dishName, quantity, totalPrice.toString())

            if (food.stock.toInt() > quantityInt) {
                dbReference.child("Cart").child(key).setValue(cart).addOnCompleteListener {
                    Toast.makeText(requireContext(), "berhasil", Toast.LENGTH_SHORT).show()

                    dbReference.child("Dish").child("Food")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                for (postSnapshot in p0.children) {
                                    val foodie = postSnapshot.getValue(Food::class.java)!!
                                    foodie.key = postSnapshot.key
                                    if (dishName == foodie.name) {
                                        var stockInt = foodie.stock!!.toInt()

                                        if (quantityInt < stockInt) {
                                            stockInt -= quantity.toInt()

                                            dbReference.child("Dish").child("Food")
                                                .child(foodie.key)
                                                .child("stock")
                                                .setValue(stockInt.toString())
                                        } else {
                                            textQuantity.error = "Maaf stok makanan masih kurang"
                                        }
                                    }
                                }
                            }
                        })
                }
            } else {
                Toast.makeText(requireContext(), "Maaf stok makanan kurang", Toast.LENGTH_SHORT)
                    .show()
                return@setPositiveButton
            }

        }

        builder.setNegativeButton("BATAL") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()

    }
}
