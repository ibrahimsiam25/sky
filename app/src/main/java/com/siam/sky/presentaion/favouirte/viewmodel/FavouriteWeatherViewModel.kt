package com.siam.sky.presentaion.favouirte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.siam.sky.core.ResponseState
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppUnit
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

class FavouriteWeatherViewModel(
    private val lat: Double,
    private val lon: Double,
    private val userRepo: UserRepo,
    private val weatherRepo: WeatherRepo
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

    init {
        observeUnitChanges()
        observeLanguageChanges()
        fetchWeather(lat, lon)
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
                    fetchHourlyForecast(state.data.name)
                    fetchDailyForecast(state.data.name)
                } else if (state is ResponseState.Error) {
                    _isRefreshing.value = false
                }
            }
        }
    }

    private fun fetchHourlyForecast(city: String) {
        hourlyJob?.cancel()
        hourlyJob = viewModelScope.launch {
            weatherRepo.getHourlyForecast(city, currentLanguage.apiLanguage, currentUnit).collect { state ->
                _hourlyState.value = state
            }
        }
    }

    private fun fetchDailyForecast(city: String) {
        dailyJob?.cancel()
        dailyJob = viewModelScope.launch {
            weatherRepo.getDailyForecast(city, currentLanguage.apiLanguage, cnt = 7, currentUnit).collect { state ->
                _dailyState.value = state
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
            weatherRepo: WeatherRepo
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FavouriteWeatherViewModel(lat, lon, userRepo, weatherRepo) as T
            }
        }
    }
}
