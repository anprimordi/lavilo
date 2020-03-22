package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.*
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.*

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment() {

    private lateinit var listCart: ArrayList<Cart>
    private lateinit var listDrink: ArrayList<Drink>
    private lateinit var listFood: ArrayList<Food>

    private lateinit var adapter: CartFragmentAdapter
    private lateinit var clickListener: CartFragmentItemClickListener
    private lateinit var dbReference: DatabaseReference

    private lateinit var exFabMakeOrder: ExtendedFloatingActionButton
    private lateinit var rvCart: RecyclerView
    private lateinit var spinnerTableNumber: Spinner
    private lateinit var textBill: TextView
    private lateinit var textTableNumber: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listCart = ArrayList()
        listFood = ArrayList()
        listDrink = ArrayList()

        rvCart = view.findViewById(R.id.rv_cart)
        textBill = view.findViewById(R.id.text_cart_bill)
        textTableNumber = view.findViewById(R.id.text_cart_table_number)

        spinnerTableNumber = view.findViewById(R.id.spinner_cart_table_number)

        rvCart.layoutManager = LinearLayoutManager(requireContext())

//        spinnerTableNumber

        dbReference = FirebaseDatabase.getInstance().getReference("Cart")
        val refTable = FirebaseDatabase.getInstance().reference.child("Table")

        clickListener = object : CartFragmentItemClickListener {
            override fun deleteCartItem(position: Int) {
                val selectedItem = listCart[position]


                val key = selectedItem.id

                dbReference.child(key).removeValue()
                adapter.notifyItemRemoved(position)

                val selectedName = selectedItem.dishName
                val selectedQuantity = selectedItem.quantity.toInt()

                val ref = FirebaseDatabase.getInstance().reference.child(key)

//                TODO add dish quantity when item in cart deleted
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val food = p0.value as Food
                        val drink = p0.value as Drink

                        for (postSnapshot in p0.children) {
                            if (selectedName == food.name) {
                                var stockItemFood = food.stock.toInt()

                                stockItemFood += selectedQuantity

                                ref.child("stock").setValue(stockItemFood.toString())
                            }
                        }

                        for (postSnapshot in p0.children) {
                            if (selectedName == drink.name) {
                                var stockItemDrink = drink.stock!!.toInt()

                                stockItemDrink += selectedQuantity

                                ref.child("stock").setValue(stockItemDrink.toString())
                            }
                        }
                    }
                })
            }
        }

        adapter = CartFragmentAdapter(requireContext(), listCart, clickListener)

        rvCart.adapter = adapter

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listCart.clear()

                for (postSnapshot in dataSnapshot.children) {
                    val cart =
                        postSnapshot.getValue(
                            Cart::class.java
                        )!!
                    listCart.add(cart)
                }
                adapter.notifyDataSetChanged()
            }
        })

        refTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                Log.e("Error Cart Fragment", p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                val listTable = ArrayList<String>()

                for (postSnapshot in p0.children) {
                    val tableNumber = postSnapshot.child("number").getValue(String::class.java)
                    listTable.add(tableNumber!!)
                }

                val tableAdapter =
                    ArrayAdapter<String>(requireContext(), R.layout.fragment_cart, listTable)

                tableAdapter.setDropDownViewResource(R.layout.fragment_cart)

                spinnerTableNumber.adapter = tableAdapter
            }
        })

        var totalBill = 0

        for (i in 0 until listCart.size) {
            totalBill += listCart[i].price.toInt()
        }

        textBill.text = totalBill.toString()

        exFabMakeOrder.setOnClickListener {
            //            TODO make order list and go to order list menu
            val refOrder: DatabaseReference = FirebaseDatabase.getInstance().getReference("Order")
            val refOrderDetail: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("OrderDetail")

            val orderKey = refOrder.push().key
//            val orderDetailKey = refOrderDetail.push().key

            val order = Order(
                orderKey!!,
                "unpaid",
                "",
                totalBill.toString(),
                spinnerTableNumber.selectedItem.toString(),
                "",
                ""
            )

            refOrder.child(orderKey).setValue(order)

            for (i in 0 until listCart.size) {
                val cart = listCart[i]

                val orderDetail = OrderDetail(
                    orderKey,
                    cart.dishName,
                    cart.quantity,
                    cart.price
                )

                refOrderDetail.child(orderKey).setValue(orderDetail)
            }

            findNavController().navigate(R.id.action_cartFragment_to_navigation_order)
        }
    }
}
