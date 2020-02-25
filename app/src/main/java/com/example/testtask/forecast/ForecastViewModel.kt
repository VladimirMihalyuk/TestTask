package com.example.testtask.forecast

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.testtask.repository.Repository

class ForecastViewModel(private val repository: Repository, application: Application)
    : AndroidViewModel(application) {
}