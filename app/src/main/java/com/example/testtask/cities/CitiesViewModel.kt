package com.example.testtask.cities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtask.database.City
import com.example.testtask.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CitiesViewModel @Inject constructor(private val repository: Repository)
    : ViewModel() {

    val allCities = repository.getAllCities()

    fun addNewCity(cityName: String){
        val city = City(name = cityName)
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewCity(city)
        }
    }

    fun deleteCity(city:City){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCity(city)
        }
    }

    fun selectCity(city: City, isSelected: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            if(isSelected){
                repository.selectCity(city)
            }else{
                repository.unselectCity(city)
            }
        }
    }


}