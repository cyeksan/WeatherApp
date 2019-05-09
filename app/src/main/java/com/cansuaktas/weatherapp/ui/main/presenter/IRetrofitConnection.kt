package com.cansuaktas.weatherapp.ui.main.presenter


import com.cansuaktas.weatherapp.ui.main.model.response.WeatherResponse

interface IRetrofitConnection {

    fun weatherRequest(responseModel: WeatherResponse)
}