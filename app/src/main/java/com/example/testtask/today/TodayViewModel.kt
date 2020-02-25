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

    fun getCurrentWeatherByCoordinates(){
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            var todayGeo =
            if(isInternetAvailable(getApplication())){
                    repository.getCurrentWeatherByCoordinatesFromInternet(27.567444F,
                        53.893009F)
            }else{
                    repository.getCurrentWeatherByCoordinatesFromCash()
            }

            withContext(Dispatchers.Main){
                _today.value = todayGeo
                _loading.value = false
                Log.d("WTF", "${today.value}")
            }
        }
    }

    fun getCurrentWeatherByCityName(){
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            var todayCity =
                if(isInternetAvailable(getApplication())){
                    repository.getCurrentWeatherByCityNameFromInternet("Minsk")
                }else{
                    repository.getCurrentWeatherByCityNameFromCash()
                }

            withContext(Dispatchers.Main){
                _today.value = todayCity
                _loading.value = false
                Log.d("WTF", "${today.value}")
            }
        }
    }

    val idOfImage = Transformations.map(today) { today ->
        application.resources.getIdentifier(today?.image,
            "drawable", application.packageName)
    }

}