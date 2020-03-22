package com.makaryostudio.lavilo.feature.management.check.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Order
import kotlinx.android.synthetic.main.fragment_check_report.*

/**
 * A simple [Fragment] subclass.
 */
class CheckReportFragment : Fragment() {

    private lateinit var listOrder: ArrayList<Order>
    private lateinit var adapter: CheckReportAdapter
    private lateinit var dbReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listOrder = ArrayList()

        rv_check_report.layoutManager = LinearLayoutManager(requireContext())

        val clickListener = object : CheckReportItemClickListener {
            override fun onItemClick(order: Order) {
                val action =
                    CheckReportFragmentDirections
                        .actionCheckReportFragmentToCheckReportDetailFragment(
                            order
                        )

                findNavController().navigate(action)
            }

            override fun onDelete(position: Int) {
                val selectedItem = listOrder[position]
                val selectedId = selectedItem.id

                dbReference.child("Order").child(selectedId!!).removeValue()
                    .addOnCompleteListener {
                        Toast.makeText(
                            requireContext(),
                            "item berhasil dihapus",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        adapter.notifyItemRemoved(position)
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        Log.e("gagal menghapus item", it.message, it.cause)
                    }
            }
        }

        adapter = CheckReportAdapter(requireContext(), listOrder, clickListener)

        rv_check_report.adapter = adapter

        dbReference.child("Order")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                    Log.d("call check report db", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listOrder.clear()

                    for (postSnapshot in p0.children) {
                        val order = postSnapshot.value as Order

                        listOrder.add(order)
                    }
                    adapter.notifyDataSetChanged()
                }
            })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_report, container, false)
    }

}
