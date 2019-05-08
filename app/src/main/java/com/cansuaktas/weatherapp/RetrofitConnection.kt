package com.cansuaktas.weatherapp

import com.cansuaktas.weatherapp.network.WeatherService
import com.cansuaktas.weatherapp.response.WeatherResponse
import com.cansuaktas.weatherapp.ui.IRetrofitConnection
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
                "2.5",
                cityName,
                "b63def9d6ca05b2808b93be36b2ee119"
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