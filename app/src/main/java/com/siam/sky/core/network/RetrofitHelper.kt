package com.siam.sky.core.network


import com.siam.sky.data.datasources.remote.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    const val APIKEY = "77e9d5fbfeacbc946fbd2ae07fc286fc"
    const val BASE_URL = "https://api.openweathermap.org/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    val weatherService: WeatherService =
        retrofit.create(WeatherService::class.java)
}