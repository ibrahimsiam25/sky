package com.siam.sky.presentaion.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.data.repo.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepo: UserRepo
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow(userRepo.getSavedAppLanguage())
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    init {
        viewModelScope.launch {
            userRepo.observeAppLanguage().collect { language ->
                _selectedLanguage.value = language
            }
        }
    }

    fun selectLanguage(language: AppLanguage): Boolean {
        if (_selectedLanguage.value == language) {
            return false
        }

        userRepo.updateAppLanguage(language)
        _selectedLanguage.value = language
        return true
    }

    companion object {
        fun factory(userRepo: UserRepo) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(userRepo) as T
            }
        }
    }
}