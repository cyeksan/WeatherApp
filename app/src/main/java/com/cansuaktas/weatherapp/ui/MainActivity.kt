package com.cansuaktas.weatherapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast

import com.cansuaktas.weatherapp.enums.WeatherType
import com.cansuaktas.weatherapp.network.WeatherService
import com.cansuaktas.weatherapp.response.WeatherResponse
import com.cansuaktas.weatherapp.ui.GetLocation
import com.cansuaktas.weatherapp.ui.IRetrofitConnection
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity(), IRetrofitConnection {

    private val weatherList = ArrayList<WeatherModel>()
    private lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private val PERMISSION_REQUEST_CODE = 1000
    private val permissions =
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )


        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkPermission(permissions)) {
                getLocation()

            } else {

                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        } else {

            getLocation()

        }

        txt_city.text = getCity(latitude!!, longitude!!)

        RetrofitConnection(getCity(latitude!!, longitude!!), this)

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

        val hour: Int = this.substring(0, 2).toInt()

        return if (hour > 12 && hour - 12 < 10) {
            "0${hour - 12}:00 PM"
        } else if (hour > 12 && hour - 12 >= 10) {
            "${hour - 12}:00 PM"
        } else {
            "$this AM"
        }
    }

    fun setBackgroundImage(weatherType: String) {

        when (weatherType) {

            WeatherType.CLOUDS.toString() -> {
                img_background.setImageResource(R.drawable.clouds)
                img_weather.setImageResource(R.drawable.ic_cloud)
            }

            WeatherType.RAIN.toString() -> {
                img_background.setImageResource(R.drawable.rain)
                img_weather.setImageResource(R.drawable.ic_rain)
            }

            WeatherType.CLEAR.toString() -> {

                img_background.setImageResource(R.drawable.clear)
                img_weather.setImageResource(R.drawable.ic_sun)
            }

            else -> {

                img_background.setImageResource(R.drawable.clear)
                img_weather.setImageResource(R.drawable.ic_sun)
            }
        }

        img_background.scaleType = ImageView.ScaleType.CENTER_CROP

    }

    fun String.addDegreeSign(): String {

        return "$this°"
    }

    fun getCity(lat: Double, lng: Double): String {

        var cityName: String? = null
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>

        try {
            addresses = geocoder.getFromLocation(lat, lng, 10)

            if (addresses.isNotEmpty()) {

                for (adr: Address in addresses) {

                    if (adr.locality != null && adr.locality.isNotEmpty()) {

                        cityName = adr.locality

                        break
                    }
                }
            }
        } catch (e: IOException) {

            e.printStackTrace()
        }

        return cityName!!
    }


    private fun getLocation() {

       GetLocation(this)
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain =
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                            permissions[i]
                        )
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess) {
                getLocation()

            }
        }
    }

    override fun weatherRequest(responseModel: WeatherResponse) {
        for (i in 0 until 3) {

            weatherList.add(WeatherModel().apply {

                time = responseModel.list[i].dtTxt.substring(11, 16).setAmPm()
                degree = responseModel.list[i].main.temp.toCelsius().formatDouble()
                weatherUrl = responseModel.list[i].weather[0].icon.getWeatherIcon()

            })


        }
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        recyclerView.adapter = WeatherAdapter(weatherList)

        txt_weather_desc.text = responseModel.list[2].weather[0].main

        txt_weather.text = responseModel.list[2].main.temp.toCelsius().formatDouble().addDegreeSign()

        setBackgroundImage(responseModel.list[2].weather[0].main)
    }

}