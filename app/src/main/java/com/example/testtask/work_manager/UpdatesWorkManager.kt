package com.example.testtask.work_manager

import android.content.Context
import androidx.work.*
import com.example.testtask.database.MyDatabase
import com.example.testtask.network.WeatherAPIClient
import com.example.testtask.repository.Repository
import java.util.concurrent.TimeUnit

val KEY_STRING = "KEY_STRING"
val WORK_NAME = "WORK_NAME"

class UpdatesWorkManager(val context: Context, workerParams: WorkerParameters)
    : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result{
        return  try {
            val client = WeatherAPIClient.getClient()
            val database = MyDatabase.getInstance(context).databaseDao
            val repository = Repository.getInstance(client, database)

            val cityName = inputData.getString(KEY_STRING)
            cityName?.let{
                repository.getCurrentWeatherByCityNameFromInternet(cityName)
                repository.getForecastByCityFromInternet(cityName)
            }
            Result.success()
        } catch (error: Throwable) {
            Result.failure()
        }
    }
}

fun createConstraints() = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.UNMETERED)
    .build()

fun createWorkRequest(city: String, minutes: Long): PeriodicWorkRequest {
    val data = Data.Builder()
    data.putString(KEY_STRING, city)

    return PeriodicWorkRequestBuilder<UpdatesWorkManager>(minutes, TimeUnit.MINUTES)  // setting period to 12 hours
        .setInputData(data.build())
        .setConstraints(createConstraints())
        .build()
}

fun startWork(city: String, minutes: Long){
    val work = createWorkRequest(city, minutes)
    WorkManager.getInstance().enqueueUniquePeriodicWork(WORK_NAME,
        ExistingPeriodicWorkPolicy.REPLACE, work)
}

fun cancelWork(){
    WorkManager.getInstance().cancelAllWork()
}
