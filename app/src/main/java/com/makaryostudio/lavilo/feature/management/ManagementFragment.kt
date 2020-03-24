package com.makaryostudio.lavilo.feature.management

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.makaryostudio.lavilo.R

/**
 * A simple [Fragment] subclass.
 */
class ManagementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardAddDish: CardView = view.findViewById(R.id.card_management_add_dish)
        val cardAddTable: CardView = view.findViewById(R.id.card_management_add_table)
        val cardAddEmployee: CardView = view.findViewById(R.id.card_management_add_employee)

        val cardCheckDish: CardView = view.findViewById(R.id.card_management_check_dish)
        val cardCheckReport: CardView = view.findViewById(R.id.card_management_check_report)

        (activity as AppCompatActivity).supportActionBar?.title =
            "Manajemen Admin"

//        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        cardAddDish.setOnClickListener {
            findNavController().navigate(R.id.action_managementFragment_to_addDishFragment)
        }

        cardAddTable.setOnClickListener {
            findNavController().navigate(R.id.action_managementFragment_to_addTableFragment)
        }

        cardAddEmployee.setOnClickListener {
            findNavController().navigate(R.id.action_managementFragment_to_addEmployeeFragment)
        }

        cardCheckDish.setOnClickListener {
            findNavController().navigate(R.id.action_managementFragment_to_checkDishFragment)
        }

        cardCheckReport.setOnClickListener {
            findNavController().navigate(R.id.action_managementFragment_to_checkReportFragment)
        }
    }
}
