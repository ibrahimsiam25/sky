package com.siam.sky.data.datasources.local

import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse

interface WeatherLocalDataSource {
    suspend fun saveCurrentWeather(lat: Double, lon: Double, weather: WeatherResponse)
    suspend fun saveHourlyForecast(lat: Double, lon: Double, hourly: HourlyForecastResponse)
    suspend fun saveDailyForecast(lat: Double, lon: Double, daily: DailyForecastResponse)
    suspend fun getWeather(lat: Double, lon: Double): WeatherResponse?
    suspend fun getHourly(lat: Double, lon: Double): HourlyForecastResponse?
    suspend fun getDaily(lat: Double, lon: Double): DailyForecastResponse?
}
