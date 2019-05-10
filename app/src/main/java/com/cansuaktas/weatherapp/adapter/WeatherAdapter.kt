package com.cansuaktas.weatherapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cansuaktas.weatherapp.R
import com.cansuaktas.weatherapp.model.WeatherModel
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter(val weatherList: ArrayList<WeatherModel>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        val itemView =
            LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_weather_item, p0, false)
        return ViewHolder(itemView)

    }

    override fun getItemCount(): Int = weatherList.size

    override fun onBindViewHolder(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
        val viewHolder = ViewHolder(p0.itemView)
        viewHolder.bindTo(p1)
    }

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        private val context: Context = itemView.context
        private val tvTime: TextView = itemView.findViewById(R.id.txt_time)
        private val tvDegree: TextView = itemView.findViewById(R.id.txt_degree)
        private val ivWeather: ImageView = itemView.findViewById(R.id.img_weather_icon)

        @SuppressLint("SimpleDateFormat")
        fun bindTo(position: Int) {
            val c = Calendar.getInstance().time

            val df = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = df.format(c)

            tvTime.text = weatherList[position].time
            tvDegree.text = "${weatherList[position].degree?.addDegreeSign()}"
            Glide.with(context).load(weatherList[position].weatherUrl).into(ivWeather)

            if (formattedDate != weatherList[position].today && weatherList[position].today != null) {

                setTextItemColor(Color.LTGRAY, 0.5f)
            } else {

                setTextItemColor(Color.DKGRAY, 1.0f)
            }
        }

        private fun setTextItemColor(color: Int, alphaValue: Float) {
            tvTime.setTextColor(color)
            tvDegree.setTextColor(color)
            ivWeather.alpha = alphaValue

        }

        private fun String.addDegreeSign(): String {

            return "$this°"

        }

    }


}