package com.waldemlav.testtask.domain.model

import android.os.Parcel
import android.os.Parcelable

data class PhotoData(
    val id: Int,
    val url: String,
    val date: Long,
    val lat: Double,
    val lng: Double
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(url)
        parcel.writeLong(date)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoData> {
        override fun createFromParcel(parcel: Parcel): PhotoData {
            return PhotoData(parcel)
        }

        override fun newArray(size: Int): Array<PhotoData?> {
            return arrayOfNulls(size)
        }
    }
}