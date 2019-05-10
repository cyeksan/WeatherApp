package com.cansuaktas.weatherapp.enums

enum class UrlParameters(private val parameter: String) {

    BASE_URL("https://api.openweathermap.org/"),
    VERSION("2.5");

    override fun toString(): String {
        return parameter
    }

}