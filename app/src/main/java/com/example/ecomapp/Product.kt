package com.example.ecomapp

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val weight: Double,
    var quantity: Int = 1,
    val imageResId: Int,
    var isInCart: String = "false"
) : Parcelable {
    // Конструктор для восстановления объекта из Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    // Упаковка объекта в Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeDouble(weight)
        parcel.writeInt(quantity)
        parcel.writeInt(imageResId)
        parcel.writeString(isInCart)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}