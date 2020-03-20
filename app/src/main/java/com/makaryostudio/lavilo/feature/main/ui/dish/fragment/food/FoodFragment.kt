package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Cart
import com.makaryostudio.lavilo.data.model.Food
import kotlinx.android.synthetic.main.fragment_food.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class FoodFragment : Fragment() {

    private lateinit var foodAdapter: FoodFragmentAdapter
    private lateinit var rvFood: RecyclerView
    private lateinit var listFood: ArrayList<Food>
    private lateinit var foodFragmentItemClickListener: FoodFragmentItemClickListener
    private val listHashMap: List<HashMap<Food, Int>> = emptyList()
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbListener: ValueEventListener
    private lateinit var mStorage: FirebaseStorage
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listFood = ArrayList()

        rvFood = view.findViewById(R.id.rv_food)
//        val mExFabCart: ExtendedFloatingActionButton = view.findViewById(R.id.exfab_go_to_cart)
        progressBar = view.findViewById(R.id.progress_food)

        foodFragmentItemClickListener = object : FoodFragmentItemClickListener {
            override fun amountClickListener(food: Food) {
                showDialog(food)
            }
        }

        foodAdapter = FoodFragmentAdapter(requireContext(), listFood, foodFragmentItemClickListener)

        rvFood.layoutManager = LinearLayoutManager(requireContext())
        rv_food.adapter = foodAdapter

        dbReference = FirebaseDatabase.getInstance().getReference("food")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listFood.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val food =
                        postSnapshot.getValue(
                            Food::class.java
                        )!!
//                    food.key = postSnapshot.key
                    listFood.add(food)
                }
                foodAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun showDialog(food: Food) {
        val builder = AlertDialog.Builder(requireContext())

        val refCart = FirebaseDatabase.getInstance().getReference("Cart")

        builder.setTitle("Tambahin makanan")

        val inflater = LayoutInflater.from(requireContext())

        val view = inflater.inflate(R.layout.dialog_dish, null)


        var quantityInt = 1
        var priceInt = food.price!!.toInt()

        val textDishName: TextView = view.findViewById(R.id.text_dish_dialog_name)
        val textQuantity: TextView = view.findViewById(R.id.text_dish_dialog_quantity)
        val textPrice: TextView = view.findViewById(R.id.text_dish_dialog_price)
        val buttonDecrease: ImageButton = view.findViewById(R.id.image_dish_dialog_decrease)
        val buttonIncrease: ImageButton = view.findViewById(R.id.image_dish_dialog_increase)

        textDishName.text = food.name

        refCart.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {

                    val dummy: Cart = childSnapshot.getValue(Cart::class.java)!!

                    if (food.name == dummy.dishName) {
                        if (dummy.quantity.toInt() != 0) {
                            quantityInt = dummy.quantity.toInt()
                        }
                    }
                }
            }
        })

        buttonDecrease.setOnClickListener {
            if (quantityInt != 0) {
                quantityInt--
            }
            priceInt /= quantityInt
            textQuantity.text = quantityInt.toString()
            textPrice.text = priceInt.toString()
        }

        buttonIncrease.setOnClickListener {
            quantityInt++
            priceInt *= quantityInt
            textQuantity.text = quantityInt.toString()
            textPrice.text = priceInt.toString()
        }

        builder.setView(view)

        builder.setPositiveButton("TAMBAH") { dialog, which ->

            val dishName = textDishName.text.toString().trim()

            val quantity = textQuantity.text.toString().trim()

            val price = textPrice.text.toString().trim()

            if (quantity.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Pesanannya nggak boleh kosong ya",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setPositiveButton
            }

            val key: String = refCart.push().key.toString()
//            val user = Users(user.id,nama,status)
            val cart = Cart(key, dishName, quantity, price)

            refCart.child(key).setValue(cart).addOnCompleteListener {
                Toast.makeText(requireContext(), "berhasil", Toast.LENGTH_SHORT).show()
            }

        }

        builder.setNegativeButton("BATAL") { dialog, which ->

        }

        val alert = builder.create()
        alert.show()

    }
}
