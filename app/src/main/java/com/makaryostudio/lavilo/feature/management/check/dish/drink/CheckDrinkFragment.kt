package com.makaryostudio.lavilo.feature.management.check.dish.drink

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Drink
import kotlinx.android.synthetic.main.fragment_check_drink.*

/**
 * A simple [Fragment] subclass.
 */
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

        listDrink = ArrayList()

        storage = FirebaseStorage.getInstance()

        dbReference = FirebaseDatabase.getInstance().reference

        rv_check_drink.layoutManager = LinearLayoutManager(requireContext())

        val clickListener = object : CheckDrinkItemClickListener {
            //implementasi menggunakan parcelable
            override fun onEdit(drink: Drink) {

                val action =
                    CheckDrinkFragmentDirections.actionCheckDrinkFragmentToEditDrinkFragment(drink)

                findNavController().navigate(action)
            }

            //implementasi menggunakan position
            override fun onDelete(position: Int) {
                val selectedItem = listDrink[position]
                val selectedKey = selectedItem.key

                val imgRef = storage.getReferenceFromUrl(selectedItem.imageUrl!!)
                imgRef.delete().addOnSuccessListener {
                    dbReference.child("Dish").child("Drink").child(selectedKey!!).removeValue()
                    Toast.makeText(requireContext(), "item berhasil dihapus", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        adapter = CheckDrinkFragmentAdapter(requireContext(), listDrink, clickListener)

        rv_check_drink.adapter = adapter

        dbListener = dbReference.child("Dish").child("Drink")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                    Log.d("call check drink db", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listDrink.clear()

                    for (postSnapshot in p0.children) {
                        val drink = postSnapshot.value as Drink

                        listDrink.add(drink)
                    }
                    adapter.notifyDataSetChanged()
                }
            })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_drink, container, false)
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        dbReference.removeEventListener(dbListener)
    }
}
