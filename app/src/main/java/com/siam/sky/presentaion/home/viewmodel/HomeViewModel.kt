package com.siam.sky.presentaion.home.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.siam.sky.core.ApiState
import com.siam.sky.data.models.WeatherResponse
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.data.repo.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class PermissionStatus { UNKNOWN, GRANTED, DENIED, PERMANENTLY_DENIED }

class HomeViewModel(
    private val userRepo: UserRepo,
    private val weatherRepo: WeatherRepo = WeatherRepo()
) : ViewModel() {

    private val _locationState = MutableStateFlow<Location?>(null)
    val locationState: StateFlow<Location?> = _locationState.asStateFlow()

    private val _permissionStatus = MutableStateFlow(PermissionStatus.UNKNOWN)
    val permissionStatus: StateFlow<PermissionStatus> = _permissionStatus.asStateFlow()

    private val _weatherState = MutableStateFlow<ApiState<WeatherResponse>>(ApiState.Idle)
    val weatherState: StateFlow<ApiState<WeatherResponse>> = _weatherState.asStateFlow()

    fun setPermissionStatus(status: PermissionStatus) {
        _permissionStatus.value = status
    }

    fun getFreshLocation() {
        viewModelScope.launch {
            userRepo.getFreshLocation().collect { location ->
                _locationState.value = location
                fetchWeather(location.latitude, location.longitude)
            }
        }
    }

    fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            weatherRepo.getCurrentWeather(lat, lon)
                .collect { state ->
                    _weatherState.value = state
                }
        }
    }

    companion object {
        fun factory(userRepo: UserRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(userRepo) as T
            }
        }
    }
}

