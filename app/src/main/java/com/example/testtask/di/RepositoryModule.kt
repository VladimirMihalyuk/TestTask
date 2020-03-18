package com.example.testtask.di

import com.example.testtask.database.DatabaseDAO
import com.example.testtask.network.OpenWeatherMapAPI
import com.example.testtask.repository.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule{
    @Provides
    @Singleton
    fun provideRepository(apiClient: OpenWeatherMapAPI, database: DatabaseDAO)
            = Repository.getInstance(apiClient, database)
}