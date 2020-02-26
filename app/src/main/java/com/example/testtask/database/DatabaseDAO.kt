package com.example.testtask.database

import androidx.lifecycle.LiveData
import androidx.room.*


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

    @Query("SELECT * FROM City ORDER BY popularity DESC")
    fun getAllCities(): LiveData<List<City>>

    @Delete
    fun deleteCity(city: City)

    @Insert
    fun insertCity(city: City)

    @Query("UPDATE City SET popularity = popularity + 1, isLastSelected = 1 WHERE id == :id")
    fun citySelected(id: Long)

    @Query ("UPDATE City SET isLastSelected = 0")
    fun unselectAll()


}