package com.makaryostudio.lavilo.feature.management.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Table
import kotlinx.android.synthetic.main.fragment_add_table.*

class AddTableFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbReference = FirebaseDatabase.getInstance().reference

        button_add_table.setOnClickListener {
            var go = true
            if (edit_add_table_number.text.toString() == "" || edit_add_table_number.text.toString() == "0") {
                edit_add_table_number.error = "Nomor meja tidak boleh kosong"
                edit_add_table_number.requestFocus()
                go = false
            }
            if (edit_add_table_capacity.text.toString() == "" || edit_add_table_capacity.text.toString() == "0") {
                edit_add_table_capacity.error = "Kapasitas meja belum diisi"
                edit_add_table_capacity.requestFocus()
                go = false
            }
            if (go) {
                uploadTable()
            }
        }
    }

    private fun uploadTable() {
        val table =
            Table(
                edit_add_table_number.text.toString(),
                edit_add_table_capacity.text.toString()
            )

        dbReference.child("Table").child(edit_add_table_number.text.toString()).setValue(table)
            .addOnCompleteListener {
                Toast.makeText(requireContext(), "Upload berhasil", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addEmployeeFragment_to_managementFragment)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("AddTableFragment", it.message!!)
            }
    }
}
