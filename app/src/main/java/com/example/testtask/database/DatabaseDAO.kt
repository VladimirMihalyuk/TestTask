package com.example.testtask.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DatabaseDAO {
    @Insert
    fun insertToday(today: Today)

    @Query("SELECT * FROM Today")
    fun getToday():List<Today>

    @Query("DELETE FROM Today")
    fun deleteToday()

    @Insert
    fun insertForecast(forecast: List<Forecast>)

    @Query("SELECT * FROM Forecast")
    fun getForecast():List<Forecast>

    @Query("DELETE FROM Forecast")
    fun deleteForecast()
}