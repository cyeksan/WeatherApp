package com.cansuaktas.weatherapp.enums

enum class IntentParameters(private val parameter: String) {

    WEATHER_LIST("weatherList"),
    CITY_NAME("cityName");

    override fun toString(): String {
        return parameter
    }

}