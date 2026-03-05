package com.siam.sky.data.datasources.remote

import com.siam.sky.core.ApiState
import com.siam.sky.core.network.RetrofitHelper
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource {
    private val weatherService: WeatherService = RetrofitHelper.weatherService

    fun getCurrentWeather(lat: Double, lon: Double): Flow<ApiState<WeatherResponse>> = flow {
        emit(ApiState.Loading)
        try {
            val response = weatherService.getCurrentWeather(lat = lat, lon = lon)
            emit(ApiState.Success(response))
        } catch (e: Exception) {
            emit(ApiState.Error(e.message ?: "Unknown error"))
        }
    }
}