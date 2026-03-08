package com.siam.sky.core.network

import android.util.Log
import com.siam.sky.data.datasources.remote.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    const val APIKEY = "a50b3547c713e7be1ec57c696006497f"
    const val BASE_URL = "https://pro.openweathermap.org/"
    private const val NETWORK_LOG_TAG = "SkyApi"

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(NETWORK_LOG_TAG, message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()


    val weatherService: WeatherService =
        retrofit.create(WeatherService::class.java)


}