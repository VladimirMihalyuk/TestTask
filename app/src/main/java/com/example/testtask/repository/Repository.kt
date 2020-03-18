package com.example.testtask.repository

import com.example.testtask.database.City
import com.example.testtask.database.DatabaseDAO
import com.example.testtask.database.Today
import com.example.testtask.forecast.toListOfModels
import com.example.testtask.network.OpenWeatherMapAPI
import com.example.testtask.network.data.CurrentWeather
import com.example.testtask.network.data.ForecastNetwork
import com.example.testtask.utils.toDatabaseObject
import com.example.testtask.utils.toListOfForecast
import com.example.testtask.utils.toListOfForecastModels
import com.example.testtask.forecast.adapter.ForecastListItem
import com.example.testtask.forecast.adapter.toListWithHeaders
import kotlinx.coroutines.Deferred
import java.util.*


class Repository private constructor(
    private val apiClient: OpenWeatherMapAPI,
    private val database: DatabaseDAO
) {
    companion object {
        fun getInstance(apiClient: OpenWeatherMapAPI, database: DatabaseDAO)
                = Repository(apiClient, database)
    }

    suspend fun getCurrentWeatherByCoordinatesFromInternet(longitude: Float,
                                               latitude: Float)
            = getCurrentFromNetwork { apiClient.getCurrentWeatherByCoordinates(longitude, latitude) }


    suspend fun getCurrentWeatherByCityNameFromInternet(city: String)
            = getCurrentFromNetwork{apiClient.getCurrentWeatherByCityName(city)}


    suspend fun getCurrentWeatherFromCash():Today?{
        return database.getToday().firstOrNull()
    }

    private suspend fun getCurrentFromNetwork(load: () -> Deferred<CurrentWeather>):Today? {
        val currentWeather = load().await()
        val today: Today = currentWeather.toDatabaseObject()
        database.deleteToday()
        database.insertToday(today)
        return today
    }

    suspend fun getForecastByCoordinatesFromInternet(longitude: Float,
                                                     latitude: Float)
            = getForecastFromNetwork { apiClient.getForecastByCoordinates(longitude, latitude) }


    suspend fun getForecastByCityFromInternet(city: String)
            = getForecastFromNetwork { apiClient.getForecastByCityName(city)}


    private suspend fun getForecastFromNetwork
                (load: () -> Deferred<ForecastNetwork>):List<ForecastListItem>{
        val forecast = load().await()
        val listFromNetwork = forecast.toListOfModels()
        val listWithHeaders =   listFromNetwork.toListWithHeaders()
        database.deleteForecast()
        database.insertForecast(listFromNetwork.toListOfForecast())
        return listWithHeaders
    }

    suspend fun getForecastFromCash():List<ForecastListItem>{
        val result = database.getForecast()
        val list = result.filter { it.date >  Calendar.getInstance().time }
        return list.toListOfForecastModels().toListWithHeaders()
    }

    fun getAllCities() = database.getAllCities()

    suspend fun addNewCity(city: City){
        database.insertCity(city)
    }

    suspend fun deleteCity(city: City){
        database.deleteCity(city)
    }

    suspend fun selectCity(city:City){
        database.citySelected(city.id)
        database.unselectAllExcept(city.id)
    }

    suspend fun unselectCity(city: City){
        database.unselectCity(city.id)
    }

    fun getSelected() = database.getAllSelected()
}
