package com.example.testtask.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.activity.ActivityViewModel
import com.example.testtask.cities.CitiesViewModel
import com.example.testtask.forecast.ForecastViewModel
import com.example.testtask.today.TodayViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>,
        Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ActivityViewModel::class)
    internal abstract fun postActivityViewModel(viewModel: ActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel::class)
    internal abstract fun postCitiesViewModel(viewModel: CitiesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel::class)
    internal abstract fun postForecastViewModel(viewModel: ForecastViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodayViewModel::class)
    internal abstract fun postTodayViewModel(viewModel: TodayViewModel): ViewModel

}