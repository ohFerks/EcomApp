package com.example.ecomapp

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val weight: String,
    val imageResId: Int
) : Parcelable {
    // Конструктор для восстановления объекта из Parcel
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readInt()
    )

    // Упаковка объекта в Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeString(weight)
        parcel.writeInt(imageResId)
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