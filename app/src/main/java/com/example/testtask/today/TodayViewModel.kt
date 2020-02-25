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


    fun getCurrentWeatherByCoordinates(){
        viewModelScope.launch(Dispatchers.IO) {
            val todayGeo = repository.getCurrentWeatherByCoordinates(27.567444F, 53.893009F){
                isInternetAvailable(getApplication())
            }
            withContext(Dispatchers.Main){
                _today.value = todayGeo
                Log.d("WTF", "${today.value}")
            }
        }
    }

    fun getCurrentWeatherByCityName(){
        viewModelScope.launch(Dispatchers.IO) {
            val todayCity = repository.getCurrentWeatherByCityName("Minsk"){
                isInternetAvailable(getApplication())
            }
            withContext(Dispatchers.Main){
                _today.value = todayCity
            }
        }
    }

    val idOfImage = Transformations.map(today) { today ->
        application.resources.getIdentifier(today?.image,
            "drawable", application.packageName)
    }

}