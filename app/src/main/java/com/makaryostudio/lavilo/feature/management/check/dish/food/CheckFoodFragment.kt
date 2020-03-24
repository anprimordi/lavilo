package com.makaryostudio.lavilo.feature.management.check.dish.food

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
import com.makaryostudio.lavilo.data.model.Food
import kotlinx.android.synthetic.main.fragment_check_food.*

/**
 * A simple [Fragment] subclass.
 */
class CheckFoodFragment : Fragment() {
    private var qty = 0
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

            override fun onUpdate(food: Food, position: Int) {

                val action =
                    CheckFoodFragmentDirections.actionCheckFoodFragmentToUpdateFoodFragment(food)

                findNavController().navigate(action)

//                showUpdateDialog(food, position)
//                val action = CheckFoodFragmentDirections.actionCheckFoodFragmentToUpdateFoodFragment(food)
//                findNavController().navigate(action)
            }

            override fun onDelete(food: Food, position: Int) {

                showDeleteDialog(food, position)

//                val selectedKey = food.key!!
//
//                dbReference.child("Dish").child("Food").child(selectedKey).removeValue()
//
//                val imgRef = storage.getReferenceFromUrl(food.imageUrl!!)
//                imgRef.delete().addOnSuccessListener {
////                    dbReference.child("Dish").child("Food").child(food.key!!).removeValue()
//                    Toast.makeText(requireContext(), "item berhasil dihapus", Toast.LENGTH_SHORT)
//                        .show()
//                    adapter.notifyItemRemoved(position)
//                }.addOnFailureListener {
//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    Log.d("Failed to delete item", it.message, it.cause)
//                }
            }
        }

        adapter = CheckFoodFragmentAdapter(requireContext(), listFood, clickListener)

        rv_check_food.adapter = adapter

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
                        food.key = postSnapshot.key
                        listFood.add(food)
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }

    private fun showDeleteDialog(food: Food, position: Int) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Hapus hidangan?")

        builder.setPositiveButton("HAPUS") { _, _ ->
            val selectedKey = food.key!!

            println(selectedKey)
            println(position)
            println(food.imageUrl!!)

            val imgRef = storage.getReferenceFromUrl(food.imageUrl!!)
            imgRef.delete().addOnCompleteListener {
                Toast.makeText(requireContext(), "berhasil menghapus gambar", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                Log.e("gagal menghapus gambar hidangan", it.message, it.cause)
            }

            dbReference.child("Dish").child("Food").child(selectedKey).removeValue()
                .addOnCompleteListener {
                    Toast.makeText(requireContext(), "berhasil", Toast.LENGTH_SHORT).show()
                    adapter.notifyItemRemoved(position)
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    Log.e("gagal menghapus hidangan", it.message, it.cause)
                }
        }

        builder.setNegativeButton("BATAL") { dialog, which ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

//    private fun showUpdateDialog(food: Food, position: Int) {
//        val builder = AlertDialog.Builder(requireContext())
//
//        builder.setTitle("Perbarui hidangan")
//
//        val view = LayoutInflater.from(requireContext()).inflate(R.layout.item_quantity, null)
//
//        builder.setView(view)
//        val textDishName: TextView = view.findViewById(R.id.text_update_stock_food_name)
//        val editStock: EditText = view.findViewById(R.id.edit_update_stock_quantity)
//        val buttonDecrease: ImageButton = view.findViewById(R.id.image_update_stock_decrease)
//        val buttonIncrease: ImageButton = view.findViewById(R.id.image_update_stock_increase)
//
//        textDishName.text = food.name
//        editStock.setText(food.stock)
//
//        qty = food.stock.toInt()
//
//        buttonDecrease.setOnClickListener {
//            if (qty != 0) {
//                qty--
//                editStock.setText(qty.toString())
//            }
//        }
//
//        buttonIncrease.setOnClickListener {
//            qty++
//            editStock.setText(qty++)
//        }
//
//        builder.setPositiveButton("PERBARUI") { dialog, which ->
//            val selectedKey = food.key!!
//            val stok = editStock.text.toString().trim()
//            println(selectedKey)
//            println(stok)
//
//            if (stok == "0" || stok == "") {
//                Toast.makeText(requireContext(), "stok tidak boleh kosong", Toast.LENGTH_SHORT)
//                    .show()
//                return@setPositiveButton
//            }
//
//            dbReference.child("Dish").child("Food").child(selectedKey).child("stock")
//                .setValue(qty.toString()).addOnSuccessListener {
//                    Toast.makeText(requireContext(), "Stok diperbarui", Toast.LENGTH_SHORT)
//                        .show()
//                }.addOnFailureListener {
//                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    Log.e("Gagal memperbarui stok", it.message, it.cause)
//                }
//            adapter.notifyItemChanged(position)
//            adapter.notifyDataSetChanged()
//        }
//
//        builder.setNegativeButton("BATAL") { dialog, which ->
//            dialog.dismiss()
//        }
//
//        val alert = builder.create()
//        alert.show()
//    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        dbReference.removeEventListener(dbListener)
    }
}
