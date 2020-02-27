package com.example.testtask.today

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.testtask.database.Today
import com.example.testtask.repository.Repository
import com.example.testtask.utils.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodayViewModel (private val repository: Repository,application: Application)
    : AndroidViewModel(application) {

    private var _today = MutableLiveData<Today?>()
    val today: LiveData<Today?>
        get() = _today

    private var _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private var _internetError = MutableLiveData<Boolean>()
    val internetError: LiveData<Boolean>
        get() = _internetError

    fun resetErrorMessage(){
        _internetError.value = false
    }

    fun getCurrentWeatherByCoordinates(longitude: Float, latitude: Float){
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try{
                var todayGeo =
                    if(isInternetAvailable(getApplication())){
                        repository.getCurrentWeatherByCoordinatesFromInternet(longitude, latitude)
                    }else{
                        repository.getCurrentWeatherFromCash()
                    }

                withContext(Dispatchers.Main){
                    _today.value = todayGeo
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    _internetError.value = true
                }
            }
            finally {
                withContext(Dispatchers.Main){
                    _loading.value = false
                }
            }

        }
    }

    fun getCurrentWeatherByCityName(cityName: String){
        _loading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val todayCity =
                    if(isInternetAvailable(getApplication())){
                        repository.getCurrentWeatherByCityNameFromInternet(cityName)
                    }else{
                        repository.getCurrentWeatherFromCash()
                    }
                withContext(Dispatchers.Main){
                    _today.value = todayCity
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

    val idOfImage = Transformations.map(today) { today ->
        application.resources.getIdentifier(today?.image,
            "drawable", application.packageName)
    }

}