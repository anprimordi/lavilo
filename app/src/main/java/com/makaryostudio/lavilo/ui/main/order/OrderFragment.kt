@file:Suppress("DEPRECATION")

package com.makaryostudio.lavilo.ui.main.order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Order
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment : Fragment() {

    private lateinit var adapter: OrderFragmentAdapter
    private lateinit var listOrder: ArrayList<Order>
    private lateinit var listener: OrderFragmentListener
    private lateinit var dbReference: DatabaseReference

    private lateinit var textEmpty: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listOrder = ArrayList()

        dbReference = FirebaseDatabase.getInstance().reference

        textEmpty = view.findViewById(R.id.text_order_empty_order)

        dbReference.child("Order").orderByChild("tableNumber")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT)
                        .show()
                    Log.e(
                        "Error Order Fragment",
                        databaseError.message,
                        databaseError.toException()
                    )
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listOrder.clear()

                    for (postSnapshot in dataSnapshot.children) {
                        val order = postSnapshot.getValue(Order::class.java)!!

                        if (order.status == "Belum dibayar") {

                            listOrder.add(order)

                        }
                    }
                    adapter.notifyDataSetChanged()
                    if (listOrder.isEmpty()) {
                        textEmpty.visibility = View.VISIBLE
                    } else if (listOrder.isNotEmpty()) {
                        textEmpty.visibility = View.GONE
                    }
                }
            })

        listener = object : OrderFragmentListener {
            override fun onClickListener(order: Order) {

                val action =
                    OrderFragmentDirections.actionNavigationOrderToOrderDetailFragment(order)

                findNavController().navigate(action)
            }
        }

        adapter = OrderFragmentAdapter(requireContext(), listOrder, listener)

        rv_order.adapter = adapter
        rv_order.layoutManager = LinearLayoutManager(requireContext())

    }
}
