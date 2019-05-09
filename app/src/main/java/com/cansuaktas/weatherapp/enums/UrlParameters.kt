package com.cansuaktas.weatherapp.enums

enum class UrlParameters(private val parameter: String) {

    BASE_URL("https://api.openweathermap.org/"),
    APP_ID("b63def9d6ca05b2808b93be36b2ee119"),
    VERSION("2.5");

    override fun toString(): String {
        return parameter
    }

}