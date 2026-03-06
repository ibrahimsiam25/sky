package com.siam.sky.core.network


import com.siam.sky.data.datasources.remote.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    const val APIKEY = "a50b3547c713e7be1ec57c696006497f"
    const val BASE_URL = "https://pro.openweathermap.org/"


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()


    val weatherService: WeatherService =
        retrofit.create(WeatherService::class.java)


}