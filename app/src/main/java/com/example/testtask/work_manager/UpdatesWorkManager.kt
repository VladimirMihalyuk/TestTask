package com.example.testtask.work_manager

import android.app.Application
import androidx.work.*
import com.example.testtask.TestApplication
import com.example.testtask.repository.Repository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

val KEY_STRING = "KEY_STRING"
val WORK_NAME = "WORK_NAME"

class UpdatesWorkManager(val application: Application, workerParams: WorkerParameters)
    : CoroutineWorker(application, workerParams) {

    @Inject
    lateinit var repository: Repository


    override suspend fun doWork(): Result{
        return  try {
            (application as TestApplication).appComponent.inject(this)

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

    return PeriodicWorkRequestBuilder<UpdatesWorkManager>(minutes, TimeUnit.MINUTES)
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
