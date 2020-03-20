package com.makaryostudio.lavilo.feature.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.makaryostudio.lavilo.R
import java.util.*

class AnActivity : AppCompatActivity(), TutorialItemClickListener {

    lateinit var recyclerView: RecyclerView
    var adapter: Adapter? = null
    lateinit var modelList: MutableList<Model>
    var databaseReference: DatabaseReference? = null
    var mDbListener: ValueEventListener? = null
    var mStorage: FirebaseStorage? = null
    lateinit var progressBar: ProgressBar
    private lateinit var fabAn: FloatingActionButton
    private lateinit var tutorialItemClickListener: TutorialItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_an)
        modelList = ArrayList()
        progressBar = findViewById(R.id.progress_an)
        fabAn = findViewById(R.id.fab_an)
        progressBar.visibility = View.VISIBLE
        recyclerView = findViewById(R.id.rv_an)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(
            this@AnActivity,
            modelList,
            tutorialItemClickListener
        )
        recyclerView.adapter = adapter
        mStorage = FirebaseStorage.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("food")
        fabAn.setOnClickListener {
            startActivity(
                Intent(this, TutorialActivity::class.java)
            )
        }
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                modelList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val model =
                        postSnapshot.getValue(
                            Model::class.java
                        )!!
                    model.key = postSnapshot.key
                    modelList.add(model)
                }
                adapter!!.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@AnActivity, databaseError.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDeleteListener(position: Int) {
        val selectedItem = modelList[position]
        val selectedKey = selectedItem.key
        val mReference = mStorage!!.getReferenceFromUrl(selectedItem.image)
        mReference.delete().addOnSuccessListener {
            databaseReference!!.child(selectedKey).removeValue()
            Toast.makeText(this@AnActivity, "item deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseReference!!.removeEventListener(mDbListener!!)
    }
}