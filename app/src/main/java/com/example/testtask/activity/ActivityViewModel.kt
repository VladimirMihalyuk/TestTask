package com.example.testtask.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testtask.repository.Repository

class ActivityViewModel(private val repository: Repository, application: Application) : AndroidViewModel(application) {

    val city = repository.getSelected()


    private val locationManager =
        application.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    val listener: LocationListener = object: LocationListener {
        override fun onLocationChanged(location: Location?) {
            if(location != null){
                removeUpdate(this)
                _location.value = location
            }

        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    @SuppressLint("MissingPermission")
    fun requestLocation(){
        locationManager.requestSingleUpdate(
            LocationManager.GPS_PROVIDER, listener, Looper.getMainLooper())
    }

    private fun removeUpdate(listener: LocationListener){
        locationManager.removeUpdates(listener)
    }
}