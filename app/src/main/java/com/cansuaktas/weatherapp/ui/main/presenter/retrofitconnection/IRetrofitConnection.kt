package com.cansuaktas.weatherapp.ui.main.presenter.retrofitconnection


import com.cansuaktas.weatherapp.ui.main.model.response.WeatherResponse

interface IRetrofitConnection {

    fun weatherRequest(responseModel: WeatherResponse)
}