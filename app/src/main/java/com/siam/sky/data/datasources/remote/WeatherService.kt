package com.siam.sky.data.datasources.remote

import com.siam.sky.core.network.RetrofitHelper
import com.siam.sky.data.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = RetrofitHelper.APIKEY,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric"
    ): WeatherResponse
}