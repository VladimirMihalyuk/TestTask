package com.example.testtask.repository

import android.util.Log
import com.example.testtask.database.DatabaseDAO
import com.example.testtask.database.Today
import com.example.testtask.network.OpenWeatherMapAPI
import com.example.testtask.utils.toDatabaseObject


class Repository private constructor(
    private val apiClient: OpenWeatherMapAPI,
    private val database: DatabaseDAO
) {
    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance(apiClient: OpenWeatherMapAPI, database: DatabaseDAO): Repository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Repository(apiClient, database)
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    suspend fun getCurrentWeatherByCoordinatesFromInternet(longitude: Float,
                                               latitude: Float):Today? {
        val currentWeather =
            apiClient.getCurrentWeatherByCoordinates(longitude, latitude).await()
        val today: Today = currentWeather.toDatabaseObject()
        database.deleteToday()
        database.insertToday(today)
        return today

    }

    suspend fun getCurrentWeatherByCoordinatesFromCash():Today?{
        return database.getToday().firstOrNull()
    }

    suspend fun getCurrentWeatherByCityNameFromInternet(city: String):Today? {
        val currentWeather =
            apiClient.getCurrentWeatherByCityName(city).await()
        val today = currentWeather.toDatabaseObject()
        database.deleteToday()
        database.insertToday(today)
        return today
    }

    suspend fun getCurrentWeatherByCityNameFromCash():Today? {
        return database.getToday().firstOrNull()
    }
}
