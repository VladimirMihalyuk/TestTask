package com.example.testtask.di

import android.app.Application
import com.example.testtask.activity.MainActivity
import com.example.testtask.cities.CitiesFragment
import com.example.testtask.forecast.ForecastFragment
import com.example.testtask.today.TodayFragment
import com.example.testtask.work_manager.UpdatesWorkManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    DatabaseModule::class,
    RepositoryModule::class,
    ViewModelModule::class
])
interface AppComponent {

    fun inject(target: ForecastFragment)

    fun inject(target: MainActivity)

    fun inject(target: CitiesFragment)

    fun inject(target: TodayFragment)

    fun inject(target: UpdatesWorkManager)

    @Component.Builder
    interface Builder {

        fun build(): AppComponent
        @BindsInstance
        fun application(application: Application): Builder
    }
}