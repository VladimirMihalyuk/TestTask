package com.example.testtask.forecast

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testtask.forecast.adapter.ForecastListItem
import com.example.testtask.repository.Repository
import com.example.testtask.utils.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForecastViewModel(private val repository: Repository, application: Application)
    : AndroidViewModel(application) {

    private var _list = MutableLiveData<List<ForecastListItem>>()
    val list: LiveData<List<ForecastListItem>>
        get() = _list

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun getForecastByCoordinates(){
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val forecastGeo =
                if(isInternetAvailable(getApplication())){
                    repository.getForecastByCoordinatesFromInternet(27.567444F,
                        53.893009F)
                }else{
                    repository.getForecastFromCash()
                }

            withContext(Dispatchers.Main){
                _list.value = forecastGeo
                _loading.value = false
            }
        }
    }

    fun getForecastByCityName(cityName: String){
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            var todayCity =
                if(isInternetAvailable(getApplication())){
                    repository.getForecastByCityFromInternet(cityName)
                }else{
                    repository.getForecastFromCash()
                }

            withContext(Dispatchers.Main){
                _list.value = todayCity
                _loading.value = false
            }
        }
    }

}