package com.makaryostudio.lavilo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.makaryostudio.lavilo.data.model.Food

class Repo {

    fun getFoodData(): LiveData<MutableList<Food>> {
        val mutableData = MutableLiveData<MutableList<Food>>()

        FirebaseFirestore.getInstance().collection("food").get().addOnSuccessListener { result ->
            val foodie = mutableListOf<Food>()
            for (document in result) {
                val imageFood = document.getString("imageFood")
                val nameFood = document.getString("nameFood")
                val priceFood = document.getString("priceFood")
                val stockFood = document.getString("stockFood")
                val key = document.getString("imageFood")
                val food = Food(
                    imageFood!!,
                    nameFood!!,
                    priceFood!!,
                    stockFood!!
                )
                foodie.add(food)
            }
            mutableData.value = foodie
        }
        return mutableData
    }
}