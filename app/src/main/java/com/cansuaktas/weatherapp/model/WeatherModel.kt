package com.cansuaktas.weatherapp.model

import android.os.Parcel
import android.os.Parcelable

class WeatherModel() : Parcelable {
    var time: String? = null
    var degree: String? = null
    var weatherUrl: String? = null
    var today: String? = null

    constructor(parcel: Parcel) : this() {
        time = parcel.readString()
        degree = parcel.readString()
        weatherUrl = parcel.readString()
        today = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(time)
        parcel.writeString(degree)
        parcel.writeString(weatherUrl)
        parcel.writeString(today)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WeatherModel> {
        override fun createFromParcel(parcel: Parcel): WeatherModel {
            return WeatherModel(parcel)
        }

        override fun newArray(size: Int): Array<WeatherModel?> {
            return arrayOfNulls(size)
        }
    }

}
