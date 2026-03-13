package com.siam.sky.presentaion.favouirte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.data.repo.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val weatherRepo: WeatherRepo
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

    companion object {
        fun factory(weatherRepo: WeatherRepo): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavouriteViewModel(weatherRepo) as T
                }
            }
        }
    }
}
