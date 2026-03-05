package com.siam.sky.data.repo

import com.siam.sky.core.ApiState
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

class WeatherRepo(
    private val weatherRemoteDataSource: WeatherRemoteDataSource = WeatherRemoteDataSource()
) {
    fun getCurrentWeather(lat: Double, lon: Double): Flow<ApiState<WeatherResponse>> =
        weatherRemoteDataSource.getCurrentWeather(lat, lon)
}