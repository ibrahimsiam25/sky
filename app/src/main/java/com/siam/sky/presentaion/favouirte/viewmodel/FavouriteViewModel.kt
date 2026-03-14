package com.siam.sky.presentaion.favouirte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.data.repo.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val weatherRepo: WeatherRepo,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _favouritesState = MutableStateFlow<List<FavouriteLocationEntity>>(emptyList())
    val favouritesState: StateFlow<List<FavouriteLocationEntity>> = _favouritesState.asStateFlow()

    init {
        viewModelScope.launch {
            weatherRepo.observeAllFavourites().collect { locations ->
                _favouritesState.value = locations
            }
        }
    }

    fun deleteFavourite(id: Int) {
        viewModelScope.launch {
            weatherRepo.deleteById(id)
        }
    }

    fun isConnected(): Boolean {
        return networkMonitor.isConnected()
    }

}
