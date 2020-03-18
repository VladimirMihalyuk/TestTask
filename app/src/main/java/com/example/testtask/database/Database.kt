package com.example.testtask.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Today::class, Forecast::class, City::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyDatabase  : RoomDatabase() {
    abstract val databaseDao: DatabaseDAO

    companion object {
        fun getInstance(application: Application): MyDatabase =
            Room.databaseBuilder(
                application,
                MyDatabase::class.java,
                "Database.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}