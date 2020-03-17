package com.makaryostudio.lavilo.feature.main.ui.dish.fragment.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Food
import kotlinx.android.synthetic.main.fragment_food.*

/**
 * A simple [Fragment] subclass.
 */
class FoodFragment : Fragment() {

    private lateinit var foodAdapter: FoodFragmentAdapter
    private lateinit var rvFood: RecyclerView
    private val listFood: List<Food> = emptyList()
    private lateinit var foodFragmentItemClickListener: FoodFragmentItemClickListener
    private val listHashMap: List<HashMap<Food, Int>> = emptyList()
    private lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFood = view.findViewById(R.id.rv_food)
        val mExFabCart: ExtendedFloatingActionButton = view.findViewById(R.id.exfab_go_to_cart)

        foodFragmentItemClickListener = object : FoodFragmentItemClickListener {
            override fun amountClickListener(quantity: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        foodAdapter = FoodFragmentAdapter(requireContext(), listFood, foodFragmentItemClickListener)

        rvFood.layoutManager = LinearLayoutManager(requireContext())
        rv_food.adapter = foodAdapter

        dbReference = FirebaseDatabase.getInstance().getReference("food")

//        mDbReference.addValueEventListener()

        mExFabCart.setOnClickListener {
            //            TODO set on click listener
        }
    }
}
