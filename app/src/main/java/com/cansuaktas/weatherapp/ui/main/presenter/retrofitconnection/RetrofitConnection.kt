package com.cansuaktas.weatherapp.ui.main.presenter.retrofitconnection

import com.cansuaktas.weatherapp.application.App
import com.cansuaktas.weatherapp.client.RetrofitClient
import com.cansuaktas.weatherapp.enums.UrlParameters
import com.cansuaktas.weatherapp.network.WeatherService
import com.cansuaktas.weatherapp.ui.main.model.response.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitConnection(cityName: String, val iRetrofitConnection: IRetrofitConnection) {


    init {

        connectToService(cityName)
    }

    private fun connectToService(cityName: String) {

        RetrofitClient.getClient()
            .create(WeatherService::class.java)
            .getWeatherData(
                UrlParameters.VERSION.toString(),
                cityName.trim(),
                App.getInstance().getApiKey(1)
            )
            .enqueue(object : Callback<WeatherResponse> {

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

                }

                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {

                    iRetrofitConnection.weatherRequest(response.body()!!)
                }
            }

            )

    }


}