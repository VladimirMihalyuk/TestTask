package com.example.testtask.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DatabaseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToday(today: Today)

    @Query("SELECT * FROM Today")
    fun getToday():List<Today>

    @Query("DELETE FROM Today")
    fun deleteToday()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecast: List<Forecast>)

    @Query("SELECT * FROM Forecast")
    fun getForecast():List<Forecast>

    @Query("DELETE FROM Forecast")
    fun deleteForecast()
}