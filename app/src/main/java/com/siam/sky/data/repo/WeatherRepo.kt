package com.siam.sky.data.repo

import com.siam.sky.core.ResponseState
import com.siam.sky.data.models.CityResponse
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    fun getCurrentWeather(lat: Double, lon: Double, language: String, unit: String): Flow<ResponseState<WeatherResponse>>
    fun getHourlyForecast(lat: Double, lon: Double, city: String, language: String, unit: String): Flow<ResponseState<HourlyForecastResponse>>
    fun getDailyForecast(lat: Double, lon: Double, city: String, language: String, cnt: Int = 7, unit: String): Flow<ResponseState<DailyForecastResponse>>
    fun searchCity(query: String): Flow<ResponseState<CityResponse>>
    fun reverseGeocode(lat: Double, lon: Double): Flow<ResponseState<CityResponse>>
    suspend fun insertFavourite(location: FavouriteLocationEntity)
    fun observeAllFavourites(): Flow<List<FavouriteLocationEntity>>
    suspend fun deleteById(id: Int)
}
