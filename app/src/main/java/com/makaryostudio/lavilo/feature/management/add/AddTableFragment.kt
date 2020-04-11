package com.makaryostudio.lavilo.feature.management.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.makaryostudio.lavilo.R
import com.makaryostudio.lavilo.data.model.Table
import kotlinx.android.synthetic.main.fragment_add_table.*

class AddTableFragment : Fragment() {

    private var capacity = 0
    private lateinit var dbReference: DatabaseReference
    private lateinit var editCapacity: EditText
    private lateinit var btnDecrease: ImageView
    private lateinit var btnIncrease: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editCapacity = view.findViewById(R.id.edit_add_table_capacity)
        btnDecrease = view.findViewById(R.id.image_add_table_decrease_capacity)
        btnIncrease = view.findViewById(R.id.image_add_table_increase_capacity)

        btnDecrease.setOnClickListener {
            if (capacity != 0) {
                capacity--
            }
            editCapacity.setText(capacity.toString())
        }

        btnIncrease.setOnClickListener {
            capacity++
            editCapacity.setText(capacity.toString())
        }

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
                Toast.makeText(requireContext(), "Meja berhasil ditambahkan", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_addTableFragment_to_managementFragment)
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("AddTableFragment", it.message!!)
            }
    }
}
