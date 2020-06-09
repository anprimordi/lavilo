package com.makaryostudio.lavilo.data.model

import android.os.Parcel
import android.os.Parcelable

// kelas model data pesanan
data class Order(
    val id: String?,
    val status: String?,
    val timestamp: String?,
    val bill: String?,
    val tableNumber: String?,
    val payment: String?,
    val change: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    constructor() : this(
        "", "", "", "", "", "", ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(status)
        parcel.writeString(timestamp)
        parcel.writeString(bill)
        parcel.writeString(tableNumber)
        parcel.writeString(payment)
        parcel.writeString(change)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}