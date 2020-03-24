package com.makaryostudio.lavilo.feature.management.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Employee
import kotlinx.android.synthetic.main.fragment_add_employee.*

class AddEmployeeFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_employee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbReference = FirebaseDatabase.getInstance().reference

//        spinner_add_employee_type.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//
//                }
//
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    val selectedItem = parent?.getItemAtPosition(position).toString()
//
//                }
//            }

        button_add_employee.setOnClickListener {
            var go = true
            if (edit_add_employee_name.text.toString() == "") {
                edit_add_employee_name.error = "Nama karyawan nggak boleh kosong"
                edit_add_employee_name.requestFocus()
                go = false
            }
            if (edit_add_employee_salary.text.toString() == "" || edit_add_employee_salary.text.toString() == "0") {
                edit_add_employee_salary.error = "Karyawan masak nggak digaji?"
                edit_add_employee_salary.requestFocus()
                go = false
            }
            if (edit_add_employee_email.text.toString() == "") {
                edit_add_employee_email.error = "Email nggak boleh kosong ya"
                edit_add_employee_email.requestFocus()
                go = false
            }
            if (edit_add_employee_password.text.toString() == "") {
                edit_add_employee_password.error = "Password nggak boleh kosong ya"
                edit_add_employee_password.requestFocus()
                go = false
            }
            if (go) {
                uploadEmployee()
            }
        }
    }

    private fun uploadEmployee() {
        val employee = Employee(
            edit_add_employee_name.text.toString(),
            edit_add_employee_email.text.toString(),
            edit_add_employee_password.text.toString(),
            edit_add_employee_salary.text.toString(),
            spinner_add_employee_type.selectedItem.toString()
        )
        val key = dbReference.child("Employee").push().key
        dbReference.child("Employee").child(key!!).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(requireContext(), "Upload berhasil", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addEmployeeFragment_to_managementFragment)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("AddEmployeeFragment", it.message!!)
            }
    }

}
