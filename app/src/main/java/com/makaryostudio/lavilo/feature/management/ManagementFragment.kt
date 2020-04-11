package com.makaryostudio.lavilo.feature.management

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.makaryostudio.lavilo.R

/**
 * A simple [Fragment] subclass.
 */
class ManagementFragment : Fragment() {

    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    //    private lateinit var buttonLogin: Button
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        authStateListener = FirebaseAuth.AuthStateListener {
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                findNavController().navigate(R.id.action_managementFragment_to_navigation_admin)
            }
        }

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

        val cardLogout: CardView = view.findViewById(R.id.card_management_logout)

//        val toolbar: MaterialToolbar = view.findViewById(R.id.toolbar_management)

//        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        (activity as AppCompatActivity).supportActionBar?.title =
            "Manajemen Admin"

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

        cardLogout.setOnClickListener {
            firebaseAuth.signOut()
//            findNavController().navigate(R.id.action_managementFragment_to_navigation_admin)
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.management_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_logout) {
            firebaseAuth.signOut()
            return true
        }

        return NavigationUI.onNavDestinationSelected(item, findNavController())
                || super.onOptionsItemSelected(item)
    }
}
