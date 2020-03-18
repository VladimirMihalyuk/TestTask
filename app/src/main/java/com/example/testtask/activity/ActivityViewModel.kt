package com.example.testtask.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testtask.repository.Repository
import com.example.testtask.work_manager.cancelWork
import com.example.testtask.work_manager.startWork
import javax.inject.Inject

class ActivityViewModel @Inject constructor(private val repository: Repository,
                                             application: Application) : AndroidViewModel(application) {
    private val _useGeolocation = MutableLiveData<Boolean>(false)
    val useGeolocation: LiveData<Boolean>
        get() = _useGeolocation

    fun useGeolocation() = _useGeolocation.value

    fun setUseGeolocation(isUsing: Boolean){
        _useGeolocation.value = isUsing
    }

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

    fun start15MinutesUpdate(){
        city.value?.firstOrNull()?.let{
            startWork(it.name, 15)
            Toast.makeText(getApplication(), "Updates for ${it.name} evey 15 minutes",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun startHourMinutesUpdate(){
        city.value?.firstOrNull()?.let{
            startWork(it.name, 60)
            Toast.makeText(getApplication(), "Updates for ${it.name} evey hour",
                Toast.LENGTH_SHORT).show()
        }
    }

    fun stopUpdates(){
        cancelWork()
        Toast.makeText(getApplication(), "All updates canceled", Toast.LENGTH_SHORT).show()
    }
}