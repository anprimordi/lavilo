package com.makaryostudio.lavilo.feature.management.check.report.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.OrderDetail
import kotlinx.android.synthetic.main.fragment_check_report.*
import kotlinx.android.synthetic.main.fragment_check_report_detail.*

/**
 * A simple [Fragment] subclass.
 */
class CheckReportDetailFragment : Fragment() {

    private lateinit var listOrderDetail: ArrayList<OrderDetail>
    private lateinit var adapter: CheckReportDetailAdapter
    private lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args: CheckReportDetailFragmentArgs by navArgs()

        val order = args.order

        listOrderDetail = ArrayList()

        text_report_detail_id.text = order.id
        text_report_detail_status.text = order.status
        text_report_detail_timestamp.text = order.timestamp
        text_report_detail_table_number.text = order.tableNumber
        text_report_detail_bill.text = order.bill

        rv_report_detail.layoutManager = LinearLayoutManager(requireContext())

        adapter = CheckReportDetailAdapter(requireContext(), listOrderDetail)

        rv_check_report.adapter = adapter

        dbReference.child("OrderDetail")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                    Log.d("call check report db", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listOrderDetail.clear()

                    for (postSnapshot in p0.children) {
                        val orderDetail = postSnapshot.value as OrderDetail

                        if (orderDetail.id == order.id) {
                            listOrderDetail.add(orderDetail)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_report_detail, container, false)
    }

}
