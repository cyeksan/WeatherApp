package com.cansuaktas.weatherapp.client

import com.cansuaktas.weatherapp.enums.UrlParameters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {

        fun getClient(): Retrofit {

            return Retrofit.Builder()
                .baseUrl(UrlParameters.BASE_URL.toString())
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}