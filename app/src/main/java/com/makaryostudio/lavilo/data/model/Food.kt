package com.makaryostudio.lavilo.data.model

import android.os.Parcel
import android.os.Parcelable

data class Food(
    val imageUrlFood: String?,
    val nameFood: String?,
    val priceFood: String?,
    val stockFood: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrlFood)
        parcel.writeString(nameFood)
        parcel.writeString(priceFood)
        parcel.writeString(stockFood)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Food> {
        override fun createFromParcel(parcel: Parcel): Food {
            return Food(parcel)
        }

        override fun newArray(size: Int): Array<Food?> {
            return arrayOfNulls(size)
        }
    }

}