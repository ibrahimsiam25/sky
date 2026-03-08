package com.siam.sky.data.datasources.remote

import com.siam.sky.BuildConfig
import com.siam.sky.core.network.RetrofitHelper
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String =  BuildConfig.APIKEY,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("data/2.5/forecast/daily")
    suspend fun getDailyForecast(
        @Query("q") city: String,
        @Query("appid") appId: String =  BuildConfig.APIKEY,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric",
        @Query("cnt") cnt: Int = 3
    ): DailyForecastResponse


    @GET("data/2.5/forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("q") city: String,
        @Query("appid") appId: String = BuildConfig.APIKEY,
        @Query("lang") lang: String = "en",
        @Query("units") units: String = "metric",
        @Query("cnt") cnt: Int = 24
    ): HourlyForecastResponse
}