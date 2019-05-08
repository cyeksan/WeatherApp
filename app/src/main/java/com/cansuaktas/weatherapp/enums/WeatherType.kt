package com.cansuaktas.weatherapp.enums

enum class WeatherType(private val weather_type: String?) {

    CLOUDS("Clouds"),
    RAIN("Rain"),
    CLEAR("Clear");

    override fun toString(): String {
        return weather_type.toString()
    }
}