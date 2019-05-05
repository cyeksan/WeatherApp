package com.cansuaktas.weatherapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.cansuaktas.weatherapp.network.WeatherService
import com.cansuaktas.weatherapp.response.WeatherResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        setContentView(R.layout.activity_main)

        RetrofitClient.getClient()
            .create(WeatherService::class.java)
            .getWeatherData("2.5", "istanbul", "b63def9d6ca05b2808b93be36b2ee119")
            .enqueue(object : Callback<WeatherResponse> {
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    Log.v("Cansu2", t.message.toString())


                }

                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {


                    txt_degree1.text = "${response.body()?.list!![0].main.temp.toCelsius().formatDouble()}°"
                    txt_degree2.text = "${response.body()?.list!![1].main.temp.toCelsius().formatDouble()}°"
                    txt_degree3.text = "${response.body()?.list!![2].main.temp.toCelsius().formatDouble()}°"

                    Toast.makeText(
                        this@MainActivity,
                        response.body()?.list!![0].main.temp.toString(),
                        Toast.LENGTH_LONG
                    ).show()

                    val time1 = response.body()?.list!![0].dtTxt.substring(11, 16)
                    val time2 = response.body()?.list!![1].dtTxt.substring(11, 16)
                    val time3 = response.body()?.list!![2].dtTxt.substring(11, 16)

                    txt_time1.text = time1.setAmPm()
                    txt_time2.text = time2.setAmPm()
                    txt_time3.text = time3.setAmPm()

                    txt_weather_desc.text = response.body()?.list!![0].weather[0].main

                    val averageTemp: Double =
                        ((response.body()?.list!![0].main.temp + response.body()?.list!![1].main.temp + response.body()?.list!![2].main.temp) / 3).toCelsius()

                    txt_weather.text = "${averageTemp.formatDouble()}°"


                    when {
                        response.body()?.list!![0].weather[0].main == "Clouds" -> {
                            img_background.setImageResource(R.drawable.clouds)
                            img_weather.setImageResource(R.drawable.ic_cloud)
                            img_background.scaleType=ImageView.ScaleType.CENTER_CROP

                        }
                        response.body()?.list!![0].weather[0].main == "Rain" -> {
                            img_background.setImageResource(R.drawable.rain)
                            img_weather.setImageResource(R.drawable.ic_rain)
                            img_background.scaleType=ImageView.ScaleType.CENTER_CROP

                        }
                        response.body()?.list!![0].weather[0].main == "Clear" -> {

                            img_background.setImageResource(R.drawable.clear)
                            img_weather.setImageResource(R.drawable.ic_sun)
                            img_background.scaleType=ImageView.ScaleType.CENTER_CROP


                        }
                }


                val imageUrl1 = response.body()?.list!![0].weather[0].icon.getWeatherIcon()
                val imageUrl2 = response.body()?.list!![1].weather[0].icon.getWeatherIcon()
                val imageUrl3 = response.body()?.list!![2].weather[0].icon.getWeatherIcon()

                Glide.with(this@MainActivity ).load(imageUrl1).into(img_weather1)
                Glide.with(this@MainActivity ).load(imageUrl2).into(img_weather2)
                Glide.with(this@MainActivity ).load(imageUrl3).into(img_weather3)
            }
    })
}

fun String.getWeatherIcon(): String {

    return "http://openweathermap.org/img/w/${this}.png"
}

fun Double.toCelsius(): Double {

    return this - 273.15
}

fun Double.formatDouble(): String {

    return if (this >= 10.0) {

        DecimalFormat("##").format(this)

    } else {
        DecimalFormat("#").format(this)
    }
}

    fun String.setAmPm(): String {

        val hour: Int = this.substring(0,2).toInt()

        return if(hour> 12) {"$this PM"}
        else {"$this AM"}
    }
}
