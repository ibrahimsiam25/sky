package com.siam.sky.data.repo

import com.siam.sky.core.ApiState
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherRepo(private val weatherRemoteDataSource: WeatherRemoteDataSource) {
    fun getCurrentWeather(lat: Double, lon: Double, language: String): Flow<ApiState<WeatherResponse>> =
        weatherRemoteDataSource.getCurrentWeather(lat, lon, language)

    fun getHourlyForecast(city: String, language: String): Flow<ApiState<HourlyForecastResponse>> =
        weatherRemoteDataSource.getHourlyForecast(city, language)

    fun getDailyForecast(city: String, language: String, cnt: Int = 7): Flow<ApiState<DailyForecastResponse>> =
        weatherRemoteDataSource.getDailyForecast(city, language, cnt)
}