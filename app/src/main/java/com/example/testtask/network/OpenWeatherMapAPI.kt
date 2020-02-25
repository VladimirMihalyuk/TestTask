package com.example.testtask.network

import com.example.testtask.network.data.CurrentWeather
import com.example.testtask.network.data.ForecastNetwork
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query


interface OpenWeatherMapAPI {

    @GET("weather")
    fun getCurrentWeatherByCoordinates(@Query("lon") longitude: Float,
                  @Query("lat") latitude: Float): Deferred<CurrentWeather>

    @GET("weather")
    fun getCurrentWeatherByCityName(@Query("q") city: String): Deferred<CurrentWeather>

    @GET("forecast")
    fun getForecastByCoordinates(@Query("lon") longitude: Float,
                          @Query("lat") latitude: Float): Deferred<ForecastNetwork>

    @GET("forecast")
    fun getForecastByCityName(@Query("q") city: String): Deferred<ForecastNetwork>
}