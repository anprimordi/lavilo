package com.makaryostudio.lavilo.feature.management.check.dish.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makaryostudio.lavilo.R

/**
 * A simple [Fragment] subclass.
 */
class CheckFoodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        TODO add create adapter to retrieve data from firebase
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_food, container, false)
    }

}
