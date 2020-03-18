package com.example.testtask.di

import android.app.Application
import com.example.testtask.database.MyDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application)
            = MyDatabase.getInstance(application).databaseDao
}