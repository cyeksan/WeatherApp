package com.cansuaktas.weatherapp.ui.detail.view

import android.content.Intent
import android.os.Bundle
import com.cansuaktas.weatherapp.adapter.WeatherAdapter
import com.cansuaktas.weatherapp.enums.IntentParameters
import com.cansuaktas.weatherapp.helper.SlideHalfScreen
import com.cansuaktas.weatherapp.model.WeatherModel
import com.cansuaktas.weatherapp.ui.main.view.MainActivity
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*

class DetailActivity : SlideHalfScreen() {

    private var weatherList = ArrayList<WeatherModel>()
    private var cityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cansuaktas.weatherapp.R.layout.activity_detail)


        cityName = intent.getStringExtra(IntentParameters.CITY_NAME.toString())
        weatherList = intent.getParcelableArrayListExtra(IntentParameters.WEATHER_LIST.toString())

        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this@DetailActivity,
            androidx.recyclerview.widget.RecyclerView.VERTICAL,
            false
        )


        recyclerView.adapter = WeatherAdapter(weatherList, true)

        fab.setOnClickListener {

            slideToHalf(motionLayout)
            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
            finish()

        }
    }


}