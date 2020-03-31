package com.makaryostudio.lavilo.feature.management.check.report

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Order
import com.makaryostudio.lavilo.data.model.OrderDetail
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOrder = ArrayList()

        dbReference = FirebaseDatabase.getInstance().reference

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                        //                        for (i in 0..10) {
//                            val key = dbReference.child("OrderDetail").push().key
//                            dbReference.child("OrderDetail").child(key!!).child("id")
//                                .child(selectedId).removeValue()
//
//                            adapter.notifyItemRemoved(position)
//                        }
//                        Toast.makeText(
//                            requireContext(),
//                            "item berhasil dihapus",
//                            Toast.LENGTH_SHORT
//                        ).show()

                        dbReference.child("OrderDetail")
                            .addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT)
                                        .show()
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    for (postSnapshot in p0.children) {
                                        val orderDetail =
                                            postSnapshot.getValue(OrderDetail::class.java)!!
                                        if (selectedId == orderDetail.id) {
                                            val key = postSnapshot.key!!
                                            dbReference.child("OrderDetail").child(key)
                                                .child("id")
                                                .child(selectedId).removeValue()

                                            adapter.notifyItemRemoved(position)
                                        }
                                    }
                                }
                            })

                        Toast.makeText(
                            requireContext(),
                            "item berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()

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
                    Log.e("call check report db", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listOrder.clear()

                    for (postSnapshot in p0.children) {
                        val order = postSnapshot.getValue(Order::class.java)!!

                        listOrder.add(order)
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.check_dish_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                showDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Hapus riwayat pesanan?")

        builder.setPositiveButton("HAPUS") { _, _ ->
            dbReference.child("Order").removeValue().addOnSuccessListener {
                Toast.makeText(requireContext(), "Riwayat berhasil dihapus", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                Log.e("Gagal menghapus riwayat", it.message, it.cause)
            }
            adapter.notifyDataSetChanged()
        }

        builder.setNegativeButton("BATAL") { dialog, _ ->
            dialog.dismiss()
        }
    }
}
