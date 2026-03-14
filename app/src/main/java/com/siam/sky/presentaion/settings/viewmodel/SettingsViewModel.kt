package com.siam.sky.presentaion.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.repo.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepo: UserRepo,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow(userRepo.getSavedAppLanguage())
    private val _selectedUnit = MutableStateFlow(userRepo.getSavedAppUnit())
    private val _selectedLocationMode = MutableStateFlow(userRepo.getSavedLocationMode())

    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()
    val selectedUnit: StateFlow<AppUnit> = _selectedUnit.asStateFlow()
    val selectedLocationMode: StateFlow<AppLoctionMode> = _selectedLocationMode.asStateFlow()

    init {
        viewModelScope.launch {
            userRepo.observeAppLanguage().collect { _selectedLanguage.value = it }
        }
        viewModelScope.launch {
            userRepo.observeUnit().collect { _selectedUnit.value = it }
        }
        viewModelScope.launch {
            userRepo.observeLocationMode().collect { _selectedLocationMode.value = it }
        }
    }

    fun selectLanguage(language: AppLanguage): Boolean {
        if (_selectedLanguage.value == language) return false
        userRepo.updateAppLanguage(language)
        _selectedLanguage.value = language
        return true
    }

    fun selectUnit(unit: AppUnit): Boolean {
        if (_selectedUnit.value == unit) return false
        userRepo.updateAppUnit(unit)
        _selectedUnit.value = unit
        return true
    }

    fun selectLocationMode(mode: AppLoctionMode) {
        userRepo.saveLocationMode(mode)
        _selectedLocationMode.value = mode
    }

    fun isConnected(): Boolean {
        return networkMonitor.isConnected()
    }

}