package com.example.testtask.di

import com.example.testtask.network.WeatherAPIClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient() = WeatherAPIClient.okHttpClient()

    @Provides
    @Singleton
    fun provideWeatherApi(okHttpClient: OkHttpClient)
            = WeatherAPIClient.provideApi(okHttpClient)
}