package com.makaryostudio.lavilo.ui.management.check.dish.drink

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Drink
import com.makaryostudio.lavilo.ui.management.check.dish.CheckDishFragmentDirections
import kotlinx.android.synthetic.main.fragment_check_drink.*

class CheckDrinkFragment : Fragment() {

    private lateinit var listDrink: ArrayList<Drink>

    private lateinit var adapter: CheckDrinkFragmentAdapter
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbListener: ValueEventListener
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_drink, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listDrink = ArrayList()

        storage = FirebaseStorage.getInstance()

        dbReference = FirebaseDatabase.getInstance().reference

        rv_check_drink.layoutManager = LinearLayoutManager(requireContext())

        val clickListener = object : CheckDrinkItemClickListener {

            override fun onUpdate(position: Int) {
                val drink = listDrink[position]
                val action =
                    CheckDishFragmentDirections
                        .actionCheckDishFragmentToUpdateDrinkFragment(drink)
                findNavController().navigate(action)
            }

            //implementasi menggunakan position
            override fun onDelete(position: Int) {
                val drinkie = listDrink[position]
                showDeleteDialog(drinkie, position)
            }
        }

        adapter = CheckDrinkFragmentAdapter(requireContext(), listDrink, clickListener)

        rv_check_drink.adapter = adapter

        dbReference.child("Dish").child("Drink")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                    Log.d("call check drink db", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listDrink.clear()

                    for (postSnapshot in p0.children) {
                        val drink = postSnapshot.getValue(Drink::class.java)!!
                        drink.key = postSnapshot.key
                        listDrink.add(drink)
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }

    private fun showDeleteDialog(drink: Drink, position: Int) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Hapus hidangan?")

        builder.setPositiveButton("HAPUS") { _, _ ->
            val selectedKey = drink.key!!

            println(selectedKey)
            println(position)
            println(drink.imageUrl!!)

            val imgRef = storage.getReferenceFromUrl(drink.imageUrl!!)
            imgRef.delete().addOnCompleteListener {
                dbReference.child("Dish").child("Drink").child(selectedKey).removeValue()
                    .addOnCompleteListener {
                        Toast.makeText(requireContext(), "berhasil", Toast.LENGTH_SHORT).show()
                        adapter.notifyItemRemoved(position)
                        adapter.notifyDataSetChanged()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        Log.e("gagal menghapus hidangan", it.message, it.cause)
                    }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                Log.e("gagal menghapus gambar hidangan", it.message, it.cause)
            }
        }

        builder.setNegativeButton("BATAL") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

}
