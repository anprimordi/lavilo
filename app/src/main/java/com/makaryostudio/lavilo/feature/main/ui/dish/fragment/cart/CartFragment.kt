package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.*
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Cart

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : Fragment() {

    private lateinit var listCart: ArrayList<Cart>

    private lateinit var dbReference: DatabaseReference
    private lateinit var adapter: CartFragmentAdapter
    private lateinit var clickListener: CartFragmentItemClickListener

    private lateinit var textBill: TextView
    private lateinit var rvCart: RecyclerView
    private lateinit var exfabMakeOrder: ExtendedFloatingActionButton


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
        rvCart = view.findViewById(R.id.rv_cart)

        rvCart.layoutManager = LinearLayoutManager(requireContext())

        dbReference = FirebaseDatabase.getInstance().getReference("cart")

        clickListener = object : CartFragmentItemClickListener {
            override fun deleteCartItem(position: Int) {
                val selectedItem = listCart[position]

                val key = selectedItem.id

                dbReference.child(key).removeValue()
                adapter.notifyItemRemoved(position)

//                TODO increase dish stock when item on cart deleted
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
                    val drink =
                        postSnapshot.getValue(
                            Cart::class.java
                        )!!
                    listCart.add(drink)
                }
                adapter.notifyDataSetChanged()
            }
        })
    }
}
