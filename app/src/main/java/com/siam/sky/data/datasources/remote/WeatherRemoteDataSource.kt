package com.siam.sky.data.datasources.remote

import com.siam.sky.core.ApiState
import com.siam.sky.core.network.RetrofitHelper
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource {
    private val weatherService: WeatherService = RetrofitHelper.weatherService

    fun getCurrentWeather(lat: Double, lon: Double, language: String): Flow<ApiState<WeatherResponse>> = flow {
        emit(ApiState.Loading)
        try {
            val response = weatherService.getCurrentWeather(lat = lat, lon = lon, lang = language)
            emit(ApiState.Success(response))
        } catch (e: Exception) {
            emit(ApiState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getHourlyForecast(city: String, language: String): Flow<ApiState<HourlyForecastResponse>> = flow {
        emit(ApiState.Loading)
        try {
            val response = weatherService.getHourlyForecast(city = city, lang = language)
            emit(ApiState.Success(response))
        } catch (e: Exception) {
            emit(ApiState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getDailyForecast(city: String, language: String, cnt: Int = 3): Flow<ApiState<DailyForecastResponse>> = flow {
        emit(ApiState.Loading)
        try {
            val response = weatherService.getDailyForecast(city = city, lang = language, cnt = cnt)
            emit(ApiState.Success(response))
        } catch (e: Exception) {
            emit(ApiState.Error(e.message ?: "Unknown error"))
        }
    }
}
