package com.cansuaktas.weatherapp.ui.main.model.response

import com.cansuaktas.weatherapp.ui.main.model.dto.CityDTO
import com.cansuaktas.weatherapp.ui.main.model.dto.WeatherInfoDTO

data class WeatherResponse(
    val cod: String,
    val message: Double,
    val cnt: Int,
    val list: ArrayList<WeatherInfoDTO>,
    val city: CityDTO
)