package com.makaryostudio.lavilo.feature.main.ui.dish.drink

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
import com.makaryostudio.lavilo.data.model.Drink
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DrinkFragment : Fragment() {

    private lateinit var adapter: DrinkFragmentAdapter
    private lateinit var rvDrink: RecyclerView
    private lateinit var listDrink: ArrayList<Drink>
    private lateinit var clickListener: DrinkFragmentItemClickListener
    private lateinit var dbReference: DatabaseReference
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drink, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvDrink = view.findViewById(R.id.rv_drink)
        progressBar = view.findViewById(R.id.progress_drink)

        listDrink = ArrayList()

        rvDrink.layoutManager = LinearLayoutManager(requireContext())

        clickListener = object : DrinkFragmentItemClickListener {
            override fun amountClickListener(drink: Drink) {
                showDialog(drink)

            }
        }

        adapter = DrinkFragmentAdapter(requireContext(), listDrink, clickListener)

        rvDrink.adapter = adapter

        dbReference = FirebaseDatabase.getInstance().reference

        dbReference.child("Dish").child("Drink").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listDrink.clear()

                for (postSnapshot in dataSnapshot.children) {
                    val drink =
                        postSnapshot.getValue(
                            Drink::class.java
                        )!!
                    drink.key = postSnapshot.key
                    listDrink.add(drink)
                }
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    @SuppressLint("InflateParams")
    fun showDialog(drink: Drink) {
        val builder = AlertDialog.Builder(requireContext())

//        builder.setTitle("Tambahin minuman")

        val inflater = LayoutInflater.from(requireContext())

        val view = inflater.inflate(R.layout.dialog_dish, null)

        val locale = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(locale)

        var quantityInt = 1
        val priceInt = drink.price!!.toInt()
        var totalPrice = priceInt

        val textDishName: TextView = view.findViewById(R.id.text_dish_dialog_name)
        val textQuantity: TextView = view.findViewById(R.id.text_dish_dialog_quantity)
        val textPrice: TextView = view.findViewById(R.id.text_dish_dialog_price)
        val buttonDecrease: ImageButton = view.findViewById(R.id.image_dish_dialog_decrease)
        val buttonIncrease: ImageButton = view.findViewById(R.id.image_dish_dialog_increase)

        val rupiah = formatRupiah.format(priceInt.toDouble())

        textPrice.text = rupiah

        textDishName.text = drink.name

        dbReference.child("Cart").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {

                    val dummy: Cart = childSnapshot.getValue(Cart::class.java)!!

                    if (drink.name == dummy.dishName) {
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

            val key = drink.key

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

            if (drink.stock.toInt() >= quantityInt) {
                dbReference.child("Cart").child(key).setValue(cart).addOnCompleteListener {
                    Toast.makeText(requireContext(), "berhasil", Toast.LENGTH_SHORT).show()

                    dbReference.child("Dish").child("Drink")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                for (postSnapshot in p0.children) {
                                    val drinkie = postSnapshot.getValue(Drink::class.java)!!
                                    drinkie.key = postSnapshot.key
                                    if (dishName == drinkie.name) {
                                        var stockInt = drinkie.stock!!.toInt()

                                        if (quantityInt <= stockInt) {
                                            stockInt -= quantity.toInt()

                                            dbReference.child("Dish").child("Drink")
                                                .child(drinkie.key)
                                                .child("stock")
                                                .setValue(stockInt.toString())
                                        } else {
                                            textQuantity.error = "Maaf stok minuman masih kurang"
                                        }

                                    }
                                }
                            }
                        })
                }
            } else {
                Toast.makeText(requireContext(), "Maaf stok minuman kurang", Toast.LENGTH_SHORT)
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
