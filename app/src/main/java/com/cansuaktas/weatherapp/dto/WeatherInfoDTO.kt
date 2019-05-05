package com.cansuaktas.weatherapp.dto

import com.google.gson.annotations.SerializedName

data class WeatherInfoDTO(val dt: Long,
                          val main: MainDTO,
                          val weather: ArrayList<WeatherDTO>,
                          val clouds: CloudsDTO,
                          val wind: WindDTO,
                          val rain: RainDTO,
                          val sys: SysDTO,
                          @SerializedName("dt_txt")
                          val dtTxt: String
)
