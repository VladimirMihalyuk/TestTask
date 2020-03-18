package com.example.testtask

import android.app.Application
import com.example.testtask.di.AppComponent
import com.example.testtask.di.DaggerAppComponent

class TestApplication: Application(){
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }

    private fun initDagger(app: Application): AppComponent =
        DaggerAppComponent.builder()
            .application(app)
            .build()
}