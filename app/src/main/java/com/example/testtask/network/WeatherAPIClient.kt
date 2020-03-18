package com.example.testtask.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val API_KEY = "85be79e8e28176b6ae2f54c4bb2dcd02"
private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

class WeatherAPIClient{
    companion object{
        fun okHttpClient():OkHttpClient{
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(ApiKeyInterceptor())
            return httpClient.build()
        }

        fun provideApi(httpClient: OkHttpClient): OpenWeatherMapAPI =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build()
                .create(OpenWeatherMapAPI::class.java)
    }
}

private class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("APPID", API_KEY)
            .build()
        val request = original.newBuilder().url(url).build()

        return chain.proceed(request)
    }
}

