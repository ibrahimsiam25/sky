package com.siam.sky.presentaion.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.data.repo.UserRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapViewModel(private val userRepo: UserRepo) : ViewModel() {

    private val saved = userRepo.getSavedLocationSource()

    private val _pickedLocation = MutableStateFlow(
        Pair(saved.first.toDouble(), saved.second.toDouble())
    )
    val pickedLocation: StateFlow<Pair<Double, Double>> = _pickedLocation.asStateFlow()

    private val _isLoadingCurrentLocation = MutableStateFlow(false)
    val isLoadingCurrentLocation: StateFlow<Boolean> = _isLoadingCurrentLocation.asStateFlow()

    // Emits a new value whenever the camera should snap to a location (GPS snap)
    private val _snapToLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val snapToLocation: StateFlow<Pair<Double, Double>?> = _snapToLocation.asStateFlow()

    private var gpsJob: Job? = null

    fun onMapTap(lat: Double, lon: Double) {
        _pickedLocation.value = Pair(lat, lon)
    }

    fun onMarkerDragged(lat: Double, lon: Double) {
        _pickedLocation.value = Pair(lat, lon)
    }

    fun useCurrentLocation() {
        gpsJob?.cancel()
        _isLoadingCurrentLocation.value = true
        gpsJob = viewModelScope.launch {
            userRepo.getFreshLocation().collect { location ->
                val pair = Pair(location.latitude, location.longitude)
                _pickedLocation.value = pair
                _snapToLocation.value = pair
                _isLoadingCurrentLocation.value = false
                gpsJob?.cancel()
            }
        }
    }

    fun confirmSelection(onDone: () -> Unit) {
        val (lat, lon) = _pickedLocation.value
        userRepo.updateLocationSource(lat.toFloat(), lon.toFloat())
        userRepo.saveLocationMode(AppLoctionMode.MAP)
        onDone()
    }

    companion object {
        fun factory(userRepo: UserRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MapViewModel(userRepo) as T
            }
        }
    }
}
