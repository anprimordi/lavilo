package com.makaryostudio.lavilo.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.Exclude

data class Drink(
    var imageUrl: String? = null,
    var name: String? = null,
    var price: String? = null,
    var stock: String? = null,
    @get:Exclude
    @set:Exclude
    var key: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    constructor() : this(
        "",
        "",
        "",
        ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageUrl)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(stock)
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