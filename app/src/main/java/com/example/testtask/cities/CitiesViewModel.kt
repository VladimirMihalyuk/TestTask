package com.example.testtask.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testtask.database.City
import com.example.testtask.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CitiesViewModel(private val repository: Repository)
    : ViewModel() {

    val allCities = repository.getAllCities()

    fun addNewCity(cityName: String){
        val city = City(name = cityName)
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewCity(city)
        }
    }
}