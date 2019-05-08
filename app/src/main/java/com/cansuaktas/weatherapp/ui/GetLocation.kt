package com.cansuaktas.weatherapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings


class GetLocation(

    private val context: Context
) {

    private lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    init {

        getLocation()
    }
    @SuppressLint("MissingPermission")
    fun getLocation() {

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps || hasNetwork) {

            if (hasGps) {

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0f,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {

                                locationGps = location
                            }
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }


                    })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }

            if (hasNetwork) {

                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0f,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {

                                locationNetwork = location
                            }
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }


                    })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null) {

                    locationNetwork = localNetworkLocation
                }

            }

            if (locationGps != null && locationNetwork == null) {
                latitude = locationGps!!.latitude
                longitude = locationGps!!.longitude

            }

            if (locationGps == null && locationNetwork != null) {
                latitude = locationNetwork!!.latitude
                longitude = locationNetwork!!.longitude

            }



            if (locationGps != null && locationNetwork != null) {

                if (locationGps!!.accuracy > locationNetwork!!.accuracy) {

                    latitude = locationGps!!.latitude
                    longitude = locationGps!!.longitude
                } else {

                    latitude = locationNetwork!!.latitude
                    longitude = locationNetwork!!.longitude
                }
            }


        } else {

            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

        }
    }
}