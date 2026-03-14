package com.siam.sky.presentaion.home.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siam.sky.R
import com.siam.sky.core.ResponseState
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
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

enum class PermissionStatus { UNKNOWN, GRANTED, DENIED, PERMANENTLY_DENIED }

class HomeViewModel(
    private val userRepo: UserRepo,
    private val weatherRepo: WeatherRepo,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private var currentLanguage: AppLanguage = userRepo.getSavedAppLanguage()
    private var currentUnit: String = userRepo.getSavedAppUnit().unit
    private var locationJob: Job? = null
    private var weatherJob: Job? = null
    private var hourlyJob: Job? = null
    private var dailyJob: Job? = null

    private val _locationState = MutableStateFlow<Location?>(null)
    val locationState: StateFlow<Location?> = _locationState.asStateFlow()

    private val _permissionStatus = MutableStateFlow(PermissionStatus.UNKNOWN)
    val permissionStatus: StateFlow<PermissionStatus> = _permissionStatus.asStateFlow()

    private val _weatherState = MutableStateFlow<ResponseState<WeatherResponse>>(ResponseState.Idle)
    val weatherState: StateFlow<ResponseState<WeatherResponse>> = _weatherState.asStateFlow()

    private val _hourlyState = MutableStateFlow<ResponseState<HourlyForecastResponse>>(ResponseState.Idle)
    val hourlyState: StateFlow<ResponseState<HourlyForecastResponse>> = _hourlyState.asStateFlow()

    private val _dailyState = MutableStateFlow<ResponseState<DailyForecastResponse>>(ResponseState.Idle)
    val dailyState: StateFlow<ResponseState<DailyForecastResponse>> = _dailyState.asStateFlow()

    private val _unitState = MutableStateFlow(userRepo.getSavedAppUnit())
    val unitState: StateFlow<AppUnit> = _unitState.asStateFlow()

    private val _showPermissionDialog = MutableStateFlow(false)
    val showPermissionDialog: StateFlow<Boolean> = _showPermissionDialog.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _toastEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val toastEvent: SharedFlow<Int> = _toastEvent

    init {
        observePermissionChanges()
        observeUnitChanges()
        observeLanguageChanges()
        observeLocationModeChanges()
        observeSavedLocationChanges()
        observeConnectivity()
    }

    fun setPermissionStatus(status: PermissionStatus) {
        _permissionStatus.value = status
    }

    fun showPermissionDialog() {
        _showPermissionDialog.value = true
    }

    fun hidePermissionDialog() {
        _showPermissionDialog.value = false
    }

    fun refresh() {
        val location = _locationState.value ?: return
        _isRefreshing.value = true
        fetchWeather(location.latitude, location.longitude)
    }

    private fun requestFreshLocation() {
        locationJob?.cancel()
        locationJob = viewModelScope.launch {
            userRepo.getFreshLocation().collect { location ->
                _locationState.value = location
                userRepo.updateLocationSource(location.latitude.toFloat(), location.longitude.toFloat())
                fetchWeather(location.latitude, location.longitude)
            }
        }
    }

    fun fetchWeather(lat: Double, lon: Double) {
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
                .drop(1) // skip initial value to avoid duplicate fetch on start
                .collect { isConnected ->
                    if (isConnected) {
                        _toastEvent.tryEmit(R.string.network_back_refreshing)
                        val location = _locationState.value
                        if (location != null) {
                            fetchWeather(location.latitude, location.longitude)
                        }
                    }
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

    private fun observeUnitChanges() {
        viewModelScope.launch {
            userRepo.observeUnit().collect { unit ->
                val changed = currentUnit != unit.unit
                currentUnit = unit.unit
                _unitState.value = unit
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
                if (status == PermissionStatus.GRANTED &&
                    userRepo.getSavedLocationMode() == AppLoctionMode.GPS
                ) {
                    val saved = userRepo.getSavedLocationSource()
                    if (saved.first != 0f && saved.second != 0f) {
                        _locationState.value = Location("map_pick").apply {
                            latitude = saved.first.toDouble()
                            longitude = saved.second.toDouble()
                        }
                        fetchWeather(saved.first.toDouble(), saved.second.toDouble())
                    } else {
                        requestFreshLocation()
                    }
                }
            }
        }
    }

    private fun observeLocationModeChanges() {
        viewModelScope.launch {
            userRepo.observeLocationMode().collect { mode ->
                when (mode) {
                    AppLoctionMode.GPS -> {
                        if (_permissionStatus.value == PermissionStatus.GRANTED) {
                            requestFreshLocation()
                        }
                    }
                    AppLoctionMode.MAP -> {
                        val saved = userRepo.getSavedLocationSource()
                        if (saved.first != 0f && saved.second != 0f) {
                            val loc = Location("map_pick").apply {
                                latitude = saved.first.toDouble()
                                longitude = saved.second.toDouble()
                            }
                            // Only trigger if location actually changed to avoid double fetch
                            val currentLoc = _locationState.value
                            if (currentLoc == null || currentLoc.latitude != saved.first.toDouble() || currentLoc.longitude != saved.second.toDouble()) {
                                _locationState.value = loc
                                fetchWeather(saved.first.toDouble(), saved.second.toDouble())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeSavedLocationChanges() {
        viewModelScope.launch {
            userRepo.observeLastKnownLocation().collect { (lat, lon) ->
                if (userRepo.getSavedLocationMode() == AppLoctionMode.MAP) {
                    if (lat == 0f && lon == 0f) {
                        userRepo.saveLocationMode(AppLoctionMode.GPS)
                        if (_permissionStatus.value == PermissionStatus.GRANTED) {
                            requestFreshLocation()
                        }
                        return@collect
                    }
                    val loc = Location("map_pick").apply {
                        latitude = lat.toDouble()
                        longitude = lon.toDouble()
                    }
                    val currentLoc = _locationState.value
                    if (currentLoc == null || currentLoc.latitude != lat.toDouble() || currentLoc.longitude != lon.toDouble()) {
                        _locationState.value = loc
                        fetchWeather(lat.toDouble(), lon.toDouble())
                    }
                }
            }
        }
    }

}



