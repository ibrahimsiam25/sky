package com.siam.sky.data.repo

import com.siam.sky.core.ResponseState
import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.datasources.local.FavouriteLocalDataSource
import com.siam.sky.data.datasources.local.WeatherLocalDataSource
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.data.models.CityResponse
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepoImp(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val cacheDataSource: WeatherLocalDataSource,
    private val favouriteLocalDataSource: FavouriteLocalDataSource,
    private val networkMonitor: NetworkMonitor
) : WeatherRepo {

    override fun getCurrentWeather(lat: Double, lon: Double, language: String, unit: String): Flow<ResponseState<WeatherResponse>> = flow {
        if (networkMonitor.isConnected()) {
            weatherRemoteDataSource.getCurrentWeather(lat, lon, language, unit).collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        cacheDataSource.saveCurrentWeather(lat, lon, state.data)
                        emit(state)
                    }

                    is ResponseState.Error -> {
                        val local = cacheDataSource.getWeather(lat, lon)
                        if (local != null) {
                            emit(ResponseState.Success(local))
                        } else {
                            emit(ResponseState.Error(state.message))
                        }
                    }

                    else -> emit(state)
                }
            }
        } else {
            val local = cacheDataSource.getWeather(lat, lon)
            if (local != null) {
                emit(ResponseState.Success(local))
            } else {
                emit(ResponseState.Error("no_internet_no_cache"))
            }
        }
    }

    override fun getHourlyForecast(lat: Double, lon: Double, city: String, language: String, unit: String): Flow<ResponseState<HourlyForecastResponse>> = flow {
        if (networkMonitor.isConnected()) {
            weatherRemoteDataSource.getHourlyForecast(city, language, unit).collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        cacheDataSource.saveHourlyForecast(lat, lon, state.data)
                        emit(state)
                    }

                    is ResponseState.Error -> {
                        val local = cacheDataSource.getHourly(lat, lon)
                        if (local != null) {
                            emit(ResponseState.Success(local))
                        } else {
                            emit(ResponseState.Error(state.message))
                        }
                    }

                    else -> emit(state)
                }
            }
        } else {
            val local = cacheDataSource.getHourly(lat, lon)
            if (local != null) {
                emit(ResponseState.Success(local))
            } else {
                emit(ResponseState.Error("no_internet_no_cache"))
            }
        }
    }

    override fun getDailyForecast(lat: Double, lon: Double, city: String, language: String, cnt: Int, unit: String): Flow<ResponseState<DailyForecastResponse>> = flow {
        if (networkMonitor.isConnected()) {
            weatherRemoteDataSource.getDailyForecast(city, language, cnt, unit).collect { state ->
                when (state) {
                    is ResponseState.Success -> {
                        cacheDataSource.saveDailyForecast(lat, lon, state.data)
                        emit(state)
                    }

                    is ResponseState.Error -> {
                        val local = cacheDataSource.getDaily(lat, lon)
                        if (local != null) {
                            emit(ResponseState.Success(local))
                        } else {
                            emit(ResponseState.Error(state.message))
                        }
                    }

                    else -> emit(state)
                }
            }
        } else {
            val local = cacheDataSource.getDaily(lat, lon)
            if (local != null) {
                emit(ResponseState.Success(local))
            } else {
                emit(ResponseState.Error("no_internet_no_cache"))
            }
        }
    }

    override fun searchCity(query: String): Flow<ResponseState<CityResponse>> = flow {
        if (networkMonitor.isConnected()) {
            weatherRemoteDataSource.searchCity(query).collect { emit(it) }
        } else {
            emit(ResponseState.Error("no_internet"))
        }
    }

    override fun reverseGeocode(lat: Double, lon: Double): Flow<ResponseState<CityResponse>> = flow {
        if (networkMonitor.isConnected()) {
            weatherRemoteDataSource.reverseGeocode(lat, lon).collect { emit(it) }
        } else {
            emit(ResponseState.Error("no_internet"))
        }
    }

    override suspend fun insertFavourite(location: FavouriteLocationEntity) {
        favouriteLocalDataSource.insertFavourite(location)
    }

    override fun observeAllFavourites(): Flow<List<FavouriteLocationEntity>> {
        return favouriteLocalDataSource.observeAllFavourites()
    }

    override suspend fun deleteById(id: Int) {
        favouriteLocalDataSource.deleteById(id)
    }
}
