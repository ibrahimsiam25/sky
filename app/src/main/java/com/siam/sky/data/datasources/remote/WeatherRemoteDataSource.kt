package com.siam.sky.data.datasources.remote

import com.siam.sky.core.ResponseState
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.core.network.RetrofitHelper
import com.siam.sky.data.models.CityResponse
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource {
    private val weatherService: WeatherService = RetrofitHelper.weatherService

    fun getCurrentWeather(lat: Double, lon: Double, language: String , unit: String): Flow<ResponseState<WeatherResponse>> = flow {
        try {
            val response = weatherService.getCurrentWeather(lat = lat, lon = lon, lang = language, units = unit)
            emit(ResponseState.Success(response))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getHourlyForecast(city: String, language: String,unit: String): Flow<ResponseState<HourlyForecastResponse>> = flow {
        try {
            val response = weatherService.getHourlyForecast(city = city, lang = language,units = unit)
            emit(ResponseState.Success(response))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "Unknown error"))
        }
    }

    fun getDailyForecast(city: String, language: String, cnt: Int = 3,unit: String): Flow<ResponseState<DailyForecastResponse>> = flow {
        try {
            val response = weatherService.getDailyForecast(city = city, lang = language, cnt = cnt,units = unit)
            emit(ResponseState.Success(response))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "Unknown error"))
        }
    }
    fun searchCity(query: String): Flow<ResponseState<CityResponse>> = flow {
        try {
            val response = weatherService.searchCity(query = query, )
            emit(ResponseState.Success(response))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "Unknown error"))
        }
    }

    fun reverseGeocode(lat: Double, lon: Double): Flow<ResponseState<CityResponse>> = flow {
        try {
            val response = weatherService.reverseGeocode(lat = lat, lon = lon)
            emit(ResponseState.Success(response))
        } catch (e: Exception) {
            emit(ResponseState.Error(e.message ?: "Unknown error"))
        }
    }
}
