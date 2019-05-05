package com.cansuaktas.weatherapp.response

import com.cansuaktas.weatherapp.dto.CityDTO
import com.cansuaktas.weatherapp.dto.WeatherInfoDTO

data class WeatherResponse(val cod: String,
                           val message: Double,
                           val cnt: Int,
                           val list: ArrayList<WeatherInfoDTO>,
                           val city: CityDTO
                           )