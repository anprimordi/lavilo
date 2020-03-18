package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.makaryostudio.lavilo.R
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
    private lateinit var fabAn: FloatingActionButton

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
            override fun amountClickListener(quantity: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

//        exfab_go_to_cart.setOnClickListener {
//            //            TODO set on click listener
//        }
    }
}
