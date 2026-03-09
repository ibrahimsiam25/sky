package com.siam.sky.data.repo

import com.siam.sky.core.ApiState
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherRepo(private val weatherRemoteDataSource: WeatherRemoteDataSource) {
    fun getCurrentWeather(lat: Double, lon: Double, language: String,unit: String): Flow<ApiState<WeatherResponse>> =
        weatherRemoteDataSource.getCurrentWeather(lat, lon, language, unit)

    fun getHourlyForecast(city: String, language: String ,unit: String): Flow<ApiState<HourlyForecastResponse>> =
        weatherRemoteDataSource.getHourlyForecast(city, language, unit)

    fun getDailyForecast(city: String, language: String, cnt: Int = 7 ,unit: String): Flow<ApiState<DailyForecastResponse>> =
        weatherRemoteDataSource.getDailyForecast(city, language, cnt,  unit)
}