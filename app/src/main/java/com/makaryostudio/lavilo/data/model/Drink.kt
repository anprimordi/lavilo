package com.makaryostudio.lavilo.data.model

import android.os.Parcel
import android.os.Parcelable

data class Drink(
    val imageUrlDrink: String?,
    val nameDrink: String?,
    val priceDrink: String?,
    val stockDrink: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrlDrink)
        parcel.writeString(nameDrink)
        parcel.writeString(priceDrink)
        parcel.writeString(stockDrink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Drink> {
        override fun createFromParcel(parcel: Parcel): Drink {
            return Drink(parcel)
        }

        override fun newArray(size: Int): Array<Drink?> {
            return arrayOfNulls(size)
        }
    }

}