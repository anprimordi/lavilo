package com.makaryostudio.lavilo.feature.management.check.dish.food

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Food
import kotlinx.android.synthetic.main.fragment_check_food.*

/**
 * A simple [Fragment] subclass.
 */
class CheckFoodFragment : Fragment() {

    private lateinit var listFood: ArrayList<Food>

    private lateinit var adapter: CheckFoodFragmentAdapter
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbListener: ValueEventListener
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listFood = ArrayList()
        storage = FirebaseStorage.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference

        rv_check_food.layoutManager = LinearLayoutManager(requireContext())

        val clickListener = object : CheckFoodItemClickListener {
            override fun onDelete(position: Int) {
                val selectedItem = listFood[position]
                val selectedKey = selectedItem.key

                val imgRef = storage.getReferenceFromUrl(selectedItem.imageUrl!!)
                imgRef.delete().addOnSuccessListener {
                    dbReference.child("Dish").child("Food").child(selectedKey!!).removeValue()
                    Toast.makeText(requireContext(), "item berhasil dihapus", Toast.LENGTH_SHORT)
                        .show()
                    adapter.notifyItemRemoved(position)
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    Log.d("Failed to delete item", it.message, it.cause)
                }
            }
        }

        adapter = CheckFoodFragmentAdapter(requireContext(), listFood, clickListener)

        rv_check_food.adapter = adapter

        dbReference.child("Dish").child("Food")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                    Log.d("call check food db", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listFood.clear()

                    for (postSnapshot in p0.children) {
                        val food = postSnapshot.getValue(Food::class.java)!!

                        listFood.add(food)
                    }
                    adapter.notifyDataSetChanged()
                }
            })

        dbListener = dbReference.child("Dish").child("Food")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                    Log.d("call check food db", p0.message)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    listFood.clear()

                    for (postSnapshot in p0.children) {
                        val food = postSnapshot.getValue(Food::class.java)!!

                        listFood.add(food)
                    }
                    adapter.notifyDataSetChanged()
                }
            })

    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        dbReference.removeEventListener(dbListener)
    }
}
