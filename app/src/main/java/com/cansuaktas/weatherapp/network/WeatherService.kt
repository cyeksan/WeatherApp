package com.cansuaktas.weatherapp.network

import com.cansuaktas.weatherapp.ui.main.model.response.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    @GET("data/{version}/forecast/hourly")
    fun getWeatherData(@Path("version") version: String,
                       @Query("q") city: String,
                       @Query("appid") appId: String) : Call<WeatherResponse>

}