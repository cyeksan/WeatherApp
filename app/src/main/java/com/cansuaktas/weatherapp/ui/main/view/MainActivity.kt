package com.cansuaktas.weatherapp.ui.main.view

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cansuaktas.weatherapp.R
import com.cansuaktas.weatherapp.adapter.WeatherAdapter
import com.cansuaktas.weatherapp.enums.IntentParameters
import com.cansuaktas.weatherapp.enums.WeatherType
import com.cansuaktas.weatherapp.helper.SlideHalfScreen
import com.cansuaktas.weatherapp.model.WeatherModel
import com.cansuaktas.weatherapp.ui.detail.view.DetailActivity
import com.cansuaktas.weatherapp.ui.main.model.response.WeatherResponse
import com.cansuaktas.weatherapp.ui.main.presenter.GetLocation
import com.cansuaktas.weatherapp.ui.main.presenter.IGetLocation
import com.cansuaktas.weatherapp.ui.main.presenter.IRetrofitConnection
import com.cansuaktas.weatherapp.ui.main.presenter.RetrofitConnection
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : SlideHalfScreen(), IRetrofitConnection, IGetLocation {

    private var cityName: String? = null
    private val list = ArrayList<WeatherModel>()
    private val weatherList: MutableList<WeatherModel> = ArrayList()
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

        checkPermissionGrantedAndGetLocation()

        fab.setOnClickListener {
            slideToHalf(motionLayout)
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(IntentParameters.CITY_NAME.toString(), cityName)
            intent.putParcelableArrayListExtra(
                IntentParameters.WEATHER_LIST.toString(),
                weatherList as java.util.ArrayList<out Parcelable>
            )
            startActivity(intent)
        }

    }

    private fun checkPermissionGrantedAndGetLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkPermission(permissions)) {
                getLocation()

            } else {

                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        } else {

            getLocation()

        }

    }

    private fun String.getWeatherIcon(): String {

        return "http://openweathermap.org/img/w/${this}.png"
    }

    private fun Double.toCelsius(): Double {

        return this - 273.15
    }

    private fun Double.formatDouble(): String {

        return if (this >= 10.0) {

            DecimalFormat("##").format(this)

        } else {
            DecimalFormat("#").format(this)
        }
    }

    private fun String.setAmPm(): String {

        val hour: Int = this.substring(0, 2).toInt()

        return if (hour > 12 && hour - 12 < 10) {
            "0${hour - 12}:00 PM"
        } else if (hour > 12 && hour - 12 >= 10) {
            "${hour - 12}:00 PM"
        } else {
            "$this AM"
        }
    }

    private fun setBackgroundImage(weatherType: String) {

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

                txt_weather.setTextColor(ContextCompat.getColor(this, R.color.iconColor))
                txt_weather_desc.setTextColor(ContextCompat.getColor(this, R.color.iconColor))
            }

            else -> {

                img_background.setImageResource(R.drawable.clear)
                img_weather.setImageResource(R.drawable.ic_sun)
            }
        }

        img_background.scaleType = ImageView.ScaleType.CENTER_CROP

    }

    private fun String.addDegreeSign(): String {

        return "$this°"
    }

    private fun getCity(lat: Double, lng: Double): String {

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

        GetLocation(this, this)
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
                        Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, getString(R.string.go_to_settings), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess) {
                getLocation()

            }
        }
    }

    override fun weatherRequest(responseModel: WeatherResponse) {


        createList(weatherList, responseModel, 2, 27)
        createList(list, responseModel, 2, 5)

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this@MainActivity,
            androidx.recyclerview.widget.RecyclerView.VERTICAL,
            false
        )
        recyclerView.adapter = WeatherAdapter(list, false)

        txt_weather_desc.text = responseModel.list[2].weather[0].main
        txt_weather.text = responseModel.list[2].main.temp.toCelsius().formatDouble().addDegreeSign()

        setBackgroundImage(responseModel.list[2].weather[0].main)
    }

    override fun getLongitudeAndLatitude(latitude: Double?, longitude: Double?) {

        txt_city.text = getCity(latitude!!, longitude!!)

        RetrofitConnection(getCity(latitude, longitude), this)

    }

    private fun createList(
        mList: MutableList<WeatherModel>,
        responseModel: WeatherResponse,
        startIndex: Int,
        endIndex: Int
    ) {

        for (i in startIndex until endIndex) {

            mList.add(WeatherModel().apply {

                time = responseModel.list[i].dtTxt.substring(11, 16).setAmPm()
                degree = responseModel.list[i].main.temp.toCelsius().formatDouble()
                weatherUrl = responseModel.list[i].weather[0].icon.getWeatherIcon()
                today = responseModel.list[i].dtTxt.substring(0, 10)

            })

        }

    }

}