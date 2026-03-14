package com.siam.sky.presentaion.favouirte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siam.sky.core.ResponseState
import com.siam.sky.data.models.CityModel
import com.siam.sky.data.models.CityResponse
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.data.repo.WeatherRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouriteMapViewModel(
    private val weatherRepo: WeatherRepo
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchState = MutableStateFlow<ResponseState<CityResponse>>(ResponseState.Idle)
    val searchState: StateFlow<ResponseState<CityResponse>> = _searchState.asStateFlow()

    // Default Cairo
    private val _pickedLocation = MutableStateFlow(Pair(30.0444, 31.2357))
    val pickedLocation: StateFlow<Pair<Double, Double>> = _pickedLocation.asStateFlow()

    private val _snapToLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val snapToLocation: StateFlow<Pair<Double, Double>?> = _snapToLocation.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchState.value = ResponseState.Idle
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchState.value = ResponseState.Loading
            weatherRepo.searchCity(query).collect { state ->
                _searchState.value = state
            }
        }
    }

    fun onCitySelected(city: CityModel) {
        _searchQuery.value = city.name
        _searchState.value = ResponseState.Idle
        val pair = Pair(city.lat, city.lon)
        _pickedLocation.value = pair
        _snapToLocation.value = pair
    }

    fun onMapTap(lat: Double, lon: Double) {
        _pickedLocation.value = Pair(lat, lon)
    }

    fun confirmSelection(onDone: () -> Unit) {
        val (lat, lon) = _pickedLocation.value
        viewModelScope.launch {
            val regionName = resolveRegionName(lat, lon)
            weatherRepo.insertFavourite(
                FavouriteLocationEntity(
                    name = regionName,
                    lat = lat,
                    lon = lon
                )
            )
            onDone()
        }
    }

    private suspend fun resolveRegionName(lat: Double, lon: Double): String {
        val searchName = _searchQuery.value.trim()
        val fallback = if (searchName.isNotBlank()) searchName else ""
        var resolvedName = fallback

        weatherRepo.reverseGeocode(lat, lon).collect { state ->
            when (state) {
                is ResponseState.Success -> {
                    val city = state.data.firstOrNull()
                    resolvedName = if (city != null) {
                        if (city.country.isNotBlank()) "${city.name}, ${city.country}" else city.name
                    } else {
                        fallback
                    }
                }
                is ResponseState.Error -> {
                    resolvedName = fallback
                }
                else -> Unit
            }
        }

        return resolvedName
    }

}
