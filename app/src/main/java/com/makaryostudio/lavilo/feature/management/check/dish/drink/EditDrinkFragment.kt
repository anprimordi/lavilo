package com.makaryostudio.lavilo.feature.management.check.dish.drink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.makaryostudio.lavilo.R

/**
 * A simple [Fragment] subclass.
 */
class EditDrinkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        TODO edit drink attributes using retrieved data from check drink fragment
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_drink, container, false)
    }

}
