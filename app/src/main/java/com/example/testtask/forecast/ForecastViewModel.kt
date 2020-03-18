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
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val repository: Repository,
                                            application: Application) : AndroidViewModel(application) {

    private var _list = MutableLiveData<List<ForecastListItem>>()
    val list: LiveData<List<ForecastListItem>>
        get() = _list

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


    private var _internetError = MutableLiveData<Boolean>()
    val internetError: LiveData<Boolean>
        get() = _internetError

    fun resetErrorMessage(){
        _internetError.value = false
    }

    fun getForecastByCoordinates(longitude: Float, latitude: Float){
        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val forecastGeo =
                    if(isInternetAvailable(getApplication())){
                        repository.getForecastByCoordinatesFromInternet(longitude,latitude)
                    }else{
                        repository.getForecastFromCash()
                    }

                withContext(Dispatchers.Main){
                    _list.value = forecastGeo
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    _internetError.value = true
                }
            }finally {
                withContext(Dispatchers.Main){
                    _loading.value = false
                }
            }
        }
    }

    fun getForecastByCityName(cityName: String){
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val todayCity =
                    if(isInternetAvailable(getApplication())){
                        repository.getForecastByCityFromInternet(cityName)
                    }else{
                        repository.getForecastFromCash()
                    }

                withContext(Dispatchers.Main){
                    _list.value = todayCity
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main){
                    _internetError.value = true
                }
            }finally {
                withContext(Dispatchers.Main){
                    _loading.value = false
                }
            }
        }
    }
}