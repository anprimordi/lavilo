package com.makaryostudio.lavilo.feature.home.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.makaryostudio.lavilo.data.Repo
import com.makaryostudio.lavilo.data.model.Food

class MenuViewModel : ViewModel() {

    private var database: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _listFood = MutableLiveData<List<Food>>()

    val listFood: LiveData<List<Food>> = _listFood

    val repo = Repo()

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is home Fragment"
//    }
//    val text: LiveData<String> = _text

    val listCart = arrayListOf<Pair<Food, Int>>()
//    TODO listCart to set quantity of food

    fun setQuantity() {
//    TODO method to set quantity of food
    }

    fun getData() {
        database.collection("food").get().addOnSuccessListener { result ->
            val foodie = mutableListOf<Food>()
            for (document in result) {
                val idFood = document.getString("idFood")
                val imageFood = document.getString("imageFood")
                val nameFood = document.getString("nameFood")
                val priceFood = document.getString("priceFood")
                val stockFood = document.getString("stockFood")
                val food = Food(
                    idFood!!.toInt(),
                    imageFood!!,
                    nameFood!!,
                    priceFood!!.toLong(),
                    stockFood!!.toInt()
                )
                foodie.add(food)
            }
            _listFood.postValue(foodie)
        }
    }

    fun fetchData(): LiveData<MutableList<Food>> {
        val mutableData = MutableLiveData<MutableList<Food>>()
        repo.getFoodData().observeForever {
            mutableData.value = it
        }
        return mutableData
    }
}