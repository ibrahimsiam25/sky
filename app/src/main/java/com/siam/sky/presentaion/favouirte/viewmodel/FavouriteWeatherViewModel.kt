package com.siam.sky.presentaion.favouirte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.siam.sky.R
import com.siam.sky.core.ResponseState
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.data.repo.WeatherRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

class FavouriteWeatherViewModel(
    private val lat: Double,
    private val lon: Double,
    private val userRepo: UserRepo,
    private val weatherRepo: WeatherRepo,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private var currentLanguage: AppLanguage = userRepo.getSavedAppLanguage()
    private var currentUnit: String = userRepo.getSavedAppUnit().unit

    private var weatherJob: Job? = null
    private var hourlyJob: Job? = null
    private var dailyJob: Job? = null

    private val _weatherState = MutableStateFlow<ResponseState<WeatherResponse>>(ResponseState.Idle)
    val weatherState: StateFlow<ResponseState<WeatherResponse>> = _weatherState.asStateFlow()

    private val _hourlyState = MutableStateFlow<ResponseState<HourlyForecastResponse>>(ResponseState.Idle)
    val hourlyState: StateFlow<ResponseState<HourlyForecastResponse>> = _hourlyState.asStateFlow()

    private val _dailyState = MutableStateFlow<ResponseState<DailyForecastResponse>>(ResponseState.Idle)
    val dailyState: StateFlow<ResponseState<DailyForecastResponse>> = _dailyState.asStateFlow()

    private val _unitState = MutableStateFlow(userRepo.getSavedAppUnit())
    val unitState: StateFlow<AppUnit> = _unitState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _toastEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val toastEvent: SharedFlow<Int> = _toastEvent

    init {
        observeUnitChanges()
        observeLanguageChanges()
        fetchWeather(lat, lon)
        observeConnectivity()
    }

    fun refresh() {
        _isRefreshing.value = true
        fetchWeather(lat, lon)
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        weatherJob?.cancel()
        hourlyJob?.cancel()
        dailyJob?.cancel()

        _weatherState.value = ResponseState.Loading
        _hourlyState.value = ResponseState.Loading
        _dailyState.value = ResponseState.Loading

        weatherJob = viewModelScope.launch {
            weatherRepo.getCurrentWeather(lat, lon, currentLanguage.apiLanguage, currentUnit).collect { state ->
                _weatherState.value = state
                if (state is ResponseState.Success) {
                    _isRefreshing.value = false
                    fetchHourlyForecast(lat, lon, state.data.name)
                    fetchDailyForecast(lat, lon, state.data.name)
                } else if (state is ResponseState.Error) {
                    _isRefreshing.value = false
                }
            }
        }
    }

    private fun fetchHourlyForecast(lat: Double, lon: Double, city: String) {
        hourlyJob?.cancel()
        hourlyJob = viewModelScope.launch {
            weatherRepo.getHourlyForecast(lat, lon, city, currentLanguage.apiLanguage, currentUnit).collect { state ->
                _hourlyState.value = state
            }
        }
    }

    private fun fetchDailyForecast(lat: Double, lon: Double, city: String) {
        dailyJob?.cancel()
        dailyJob = viewModelScope.launch {
            weatherRepo.getDailyForecast(lat, lon, city, currentLanguage.apiLanguage, cnt = 7, currentUnit).collect { state ->
                _dailyState.value = state
            }
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            networkMonitor.observeConnectivity()
                .drop(1)
                .collect { isConnected ->
                    if (isConnected) {
                        _toastEvent.tryEmit(R.string.network_back_refreshing)
                        fetchWeather(lat, lon)
                    }
                }
        }
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            userRepo.observeAppLanguage().collect { language ->
                val changed = currentLanguage != language
                currentLanguage = language
                if (changed) {
                    fetchWeather(lat, lon)
                }
            }
        }
    }

    private fun observeUnitChanges() {
        viewModelScope.launch {
            userRepo.observeUnit().collect { unit ->
                val changed = currentUnit != unit.unit
                currentUnit = unit.unit
                _unitState.value = unit
                if (changed) {
                    fetchWeather(lat, lon)
                }
            }
        }
    }

    companion object {
        fun factory(
            lat: Double,
            lon: Double,
            userRepo: UserRepo,
            weatherRepo: WeatherRepo,
            networkMonitor: NetworkMonitor
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavouriteWeatherViewModel(lat, lon, userRepo, weatherRepo, networkMonitor) as T
            }
        }
    }
}
