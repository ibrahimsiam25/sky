package com.siam.sky.presentaion.favouirte.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.data.repo.FavouriteRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val favouriteRepo: FavouriteRepo
) : ViewModel() {

    private val _favouritesState = MutableStateFlow<List<FavouriteLocationEntity>>(emptyList())
    val favouritesState: StateFlow<List<FavouriteLocationEntity>> = _favouritesState.asStateFlow()

    init {
        viewModelScope.launch {
            favouriteRepo.observeAllFavourites().collect { locations ->
                _favouritesState.value = locations
            }
        }
    }

    fun deleteFavourite(id: Int) {
        viewModelScope.launch {
            favouriteRepo.deleteById(id)
        }
    }

    companion object {
        fun factory(favouriteRepo: FavouriteRepo): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavouriteViewModel(favouriteRepo) as T
                }
            }
        }
    }
}
