package com.siam.sky.presentaion.home.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.siam.sky.core.ApiState
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.data.repo.WeatherRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class PermissionStatus { UNKNOWN, GRANTED, DENIED, PERMANENTLY_DENIED }

class HomeViewModel(
    private val userRepo: UserRepo,
    private val weatherRepo: WeatherRepo
) : ViewModel() {

    private var currentLanguage: AppLanguage = userRepo.getSavedAppLanguage()
    private var locationJob: Job? = null
    private var weatherJob: Job? = null
    private var hourlyJob: Job? = null
    private var dailyJob: Job? = null

    private val _locationState = MutableStateFlow<Location?>(null)
    val locationState: StateFlow<Location?> = _locationState.asStateFlow()

    private val _permissionStatus = MutableStateFlow(PermissionStatus.UNKNOWN)
    val permissionStatus: StateFlow<PermissionStatus> = _permissionStatus.asStateFlow()

    private val _weatherState = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Idle)
    val weatherState: StateFlow<ApiState<WeatherResponse>> = _weatherState.asStateFlow()

    private val _hourlyState = MutableStateFlow<ApiState<HourlyForecastResponse>>(ApiState.Idle)
    val hourlyState: StateFlow<ApiState<HourlyForecastResponse>> = _hourlyState.asStateFlow()

    private val _dailyState = MutableStateFlow<ApiState<DailyForecastResponse>>(ApiState.Idle)
    val dailyState: StateFlow<ApiState<DailyForecastResponse>> = _dailyState.asStateFlow()

    init {
        observeLanguageChanges()
        observePermissionChanges()
    }

    fun setPermissionStatus(status: PermissionStatus) {
        _permissionStatus.value = status
    }

    private fun requestFreshLocation() {
        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            userRepo.getFreshLocation().collect { location ->
                _locationState.value = location
                fetchWeather(location.latitude, location.longitude)
            }
        }
    }

    fun fetchWeather(lat: Double, lon: Double) {
        weatherJob?.cancel()
        hourlyJob?.cancel()
        dailyJob?.cancel()

        weatherJob = viewModelScope.launch {
            weatherRepo.getCurrentWeather(lat, lon, currentLanguage.apiLanguage).collect { state ->
                _weatherState.value = state
                if (state is ApiState.Success) {
                    fetchHourlyForecast(state.data.name)
                    fetchDailyForecast(state.data.name)
                }
            }
        }
    }

    private fun fetchHourlyForecast(city: String) {
        hourlyJob?.cancel()
        hourlyJob = viewModelScope.launch {
            weatherRepo.getHourlyForecast(city, currentLanguage.apiLanguage).collect { state ->
                _hourlyState.value = state
            }
        }
    }

    private fun fetchDailyForecast(city: String) {
        dailyJob?.cancel()
        dailyJob = viewModelScope.launch {
            weatherRepo.getDailyForecast(city, currentLanguage.apiLanguage, cnt = 7).collect { state ->
                _dailyState.value = state
            }
        }
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            userRepo.observeAppLanguage().collect { language ->
                val changed = currentLanguage != language
                currentLanguage = language

                val currentLocation = _locationState.value
                if (changed && currentLocation != null) {
                    fetchWeather(currentLocation.latitude, currentLocation.longitude)
                }
            }
        }
    }

    private fun observePermissionChanges() {
        viewModelScope.launch {
            _permissionStatus.collect { status ->
                if (status == PermissionStatus.GRANTED && _locationState.value == null) {
                    requestFreshLocation()
                }
            }
        }
    }

    companion object {
        fun factory(userRepo: UserRepo, weatherRepo: WeatherRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(userRepo, weatherRepo ) as T
            }
        }
    }
}


