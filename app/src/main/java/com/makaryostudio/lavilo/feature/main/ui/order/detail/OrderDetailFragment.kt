package com.makaryostudio.lavilo.feature.main.ui.order.detail

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Order
import com.makaryostudio.lavilo.data.model.OrderDetail
import kotlinx.android.synthetic.main.fragment_order_detail.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderDetailFragment : Fragment() {

    private lateinit var listOrderDetail: ArrayList<OrderDetail>
    private lateinit var adapter: OrderDetailFragmentAdapter
    private lateinit var dbReference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args: OrderDetailFragmentArgs by navArgs()

        val receivedOrderId = args.order!!.id

        adapter = OrderDetailFragmentAdapter(requireContext(), listOrderDetail)

        rv_order_detail.adapter = adapter

        listOrderDetail = ArrayList()

        rv_order_detail.layoutManager = LinearLayoutManager(requireContext())

        dbReference = FirebaseDatabase.getInstance().reference

        dbReference.child("OrderDetail").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), databaseError.message, Toast.LENGTH_SHORT).show()
                Log.d("Order detail fragment", databaseError.message)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOrderDetail.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val orderDetail = postSnapshot.value as OrderDetail

                    if (orderDetail.id == receivedOrderId) {
                        listOrderDetail.add(orderDetail)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        })

        text_order_detail_bill.text = args.order!!.bill

        Dexter.withActivity(requireActivity())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
//                    TODO implement print to pdf feature
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {

                }
            })
            .check()

        button_order_list_payment.setOnClickListener {
            updateOrderStatus(receivedOrderId)
        }

        return inflater.inflate(R.layout.fragment_order_detail, container, false)
    }

    private fun updateOrderStatus(receivedOrderId: String?) {
        dbReference.child("Order").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                Log.d("Order detail fragment", p0.message)
            }

            val calendar = Calendar.getInstance()
            var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", Locale.getDefault())

            val timestamp: String = simpleDateFormat.format(calendar.time)

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children) {
                    var order = postSnapshot.value as Order

                    if (order.id == receivedOrderId) {
                        order = Order(
                            receivedOrderId,
                            "paid",
                            timestamp,
                            order.bill,
                            order.tableNumber,
                            order.bill,
                            "0"
                        )

                        dbReference.child("Order").child(receivedOrderId!!).setValue(order)
                        
                        findNavController().navigate(R.id.action_orderDetailFragment_to_navigation_order)
                    }
                }
            }
        })
    }
}
