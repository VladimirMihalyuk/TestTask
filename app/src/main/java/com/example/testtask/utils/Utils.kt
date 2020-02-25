package com.example.testtask.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.testtask.database.Today
import com.example.testtask.network.data.CurrentWeather
import kotlin.math.roundToInt

fun isInternetAvailable(context: Context?): Boolean {
    var result = false
    context?.let{
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
    }
    return result
}

val directions = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
private val roundDegree = 360.0
fun windDegreeToDirection(degree: Int): String  =
    directions[((degree % roundDegree)  / (roundDegree / directions.size)). toInt()]

fun Double?.kelvinToCelsius() = (this?.toInt() ?: 273) - 273

fun CurrentWeather.toDatabaseObject(): Today {
    val image = "i" + (this.weather?.getOrNull(0)?.icon ?: "01d")
    val  city = "${this.name}, ${this.sys?.country}"
    val temperatureValue = this.main?.temp?.kelvinToCelsius() ?: 0
    val temperature = "${temperatureValue}°C |${this.weather?.getOrNull(0)?.main}"
    val cloudiness = "${(this.clouds?.all ?: 0)}%"

    val precipitationValue = ((this.snow?.threeHours ?:0.0) +
            (this.rain?.threeHours ?: 0.0))
    val precipitation = "${(precipitationValue * 10.0).roundToInt() / 10.0 } mm"
    val pressure =  "${this.main?.pressure} hPa"
    val speedValue = ((this.wind?.speed ?: 0.0) * 3.6).toInt()
    val windSpeed = "${speedValue} km/h"
    val windDirection = "${windDegreeToDirection(
        this.wind?.deg ?: 0
    )}"
    val today = Today(
        image,
        city,
        temperature,
        cloudiness,
        precipitation,
        pressure,
        windSpeed,
        windDirection
    )
    return today
}