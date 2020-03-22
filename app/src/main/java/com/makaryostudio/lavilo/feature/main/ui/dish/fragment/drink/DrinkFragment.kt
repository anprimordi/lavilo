package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.drink

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

        dbReference = FirebaseDatabase.getInstance().getReference("Dish").child("Drink")

        dbReference.addValueEventListener(object : ValueEventListener {
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

    fun showDialog(drink: Drink) {
        val builder = AlertDialog.Builder(requireContext())

        val refCart = FirebaseDatabase.getInstance().getReference("Cart")

        builder.setTitle("Tambahin minuman")

        val inflater = LayoutInflater.from(requireContext())

        val view = inflater.inflate(R.layout.dialog_dish, null)

        var quantityInt = 1
        var priceInt = drink.price!!.toInt()

        val textDishName: TextView = view.findViewById(R.id.text_dish_dialog_name)
        val textQuantity: TextView = view.findViewById(R.id.text_dish_dialog_quantity)
        val textPrice: TextView = view.findViewById(R.id.text_dish_dialog_price)
        val buttonDecrease: ImageButton = view.findViewById(R.id.image_dish_dialog_decrease)
        val buttonIncrease: ImageButton = view.findViewById(R.id.image_dish_dialog_increase)

        textDishName.text = drink.name

        refCart.addListenerForSingleValueEvent(object : ValueEventListener {
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
                        }
                    }
                }
            }
        })

        buttonDecrease.setOnClickListener {
            if (quantityInt != 0) {
                quantityInt--
            }
            priceInt /= quantityInt
            textQuantity.text = quantityInt.toString()
            textPrice.text = priceInt.toString()
        }

        buttonIncrease.setOnClickListener {
            quantityInt++
            priceInt *= quantityInt
            textQuantity.text = quantityInt.toString()
            textPrice.text = priceInt.toString()
        }

        builder.setView(view)

        builder.setPositiveButton("TAMBAH") { dialog, which ->


            val dishName = textDishName.text.toString().trim()

            val quantity = textQuantity.text.toString().trim()

            val price = textPrice.text.toString().trim()

            if (quantity.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Pesanannya nggak boleh kosong ya",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setPositiveButton
            }

//            val key: String = refCart.push().key.toString()
            val key = drink.key
            val cart = Cart(key!!, dishName, quantity, price)

//            TODO decrease drink quantity when cart item added
            refCart.child(key).setValue(cart).addOnCompleteListener {
                Toast.makeText(requireContext(), "berhasil", Toast.LENGTH_SHORT).show()

                dbReference.child(key)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            for (postSnapshot in p0.children) {
                                val drinkie = postSnapshot.value as Drink
                                if (dishName == drinkie.name) {
                                    var stockInt = drinkie.stock!!.toInt()
                                    stockInt -= quantity.toInt()

                                    dbReference.child(key).child("quantity")
                                        .setValue(stockInt.toString())
                                }
                            }
                        }
                    })
            }

        }

        builder.setNegativeButton("BATAL") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()

    }
}
