package com.example.testtask.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActivityViewModel: ViewModel() {
    private val _city = MutableLiveData<String>()
    val city: LiveData<String>
        get() = _city


    fun setCityName(city: String){
        _city.value = city
    }
}