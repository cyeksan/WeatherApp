package com.cansuaktas.weatherapp.ui.main.model.dto

data class MainDTO(val temp: Double,
                   val temp_min: Double,
                   val temp_max: Double,
                   val pressure: Double,
                   val sea_level: Double,
                   val grnd_level: Double,
                   val humidity: Int,
                   val temp_kf: Double)
