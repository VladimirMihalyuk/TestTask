package com.example.testtask.today

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtask.repository.Repository
import com.example.testtask.utils.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodayViewModel (private val repository: Repository, application: Application)
    : AndroidViewModel(application) {

    fun getCurrentWeatherByCoordinates(){
        viewModelScope.launch(Dispatchers.IO) {
            val today = repository.getCurrentWeatherByCoordinates(27.567444F, 53.893009F){
                isInternetAvailable(getApplication())
            }
            Log.d("WTF", "Geolocation:$today")
        }
    }

    fun getCurrentWeatherByCityName(){
        viewModelScope.launch(Dispatchers.IO) {
            val todayCity = repository.getCurrentWeatherByCityName("Minsk"){
                isInternetAvailable(getApplication())
            }
            Log.d("WTF", "City:$todayCity")
        }
    }
}