package com.siam.sky.data.datasources.remote

import com.siam.sky.core.ResponseState
import com.siam.sky.data.models.CityResponse
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {
    fun getCurrentWeather(lat: Double, lon: Double, language: String, unit: String): Flow<ResponseState<WeatherResponse>>
    fun getHourlyForecast(city: String, language: String, unit: String): Flow<ResponseState<HourlyForecastResponse>>
    fun getDailyForecast(city: String, language: String, cnt: Int = 3, unit: String): Flow<ResponseState<DailyForecastResponse>>
    fun searchCity(query: String): Flow<ResponseState<CityResponse>>
    fun reverseGeocode(lat: Double, lon: Double): Flow<ResponseState<CityResponse>>
}
