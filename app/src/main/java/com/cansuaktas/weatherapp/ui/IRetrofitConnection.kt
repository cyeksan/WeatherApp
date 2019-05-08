package com.cansuaktas.weatherapp.ui

import com.cansuaktas.weatherapp.response.WeatherResponse

interface IRetrofitConnection {

    fun weatherRequest(responseModel: WeatherResponse)
}