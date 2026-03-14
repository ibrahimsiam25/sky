package com.siam.sky.presentaion.alerts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.siam.sky.data.models.AlertModel
import com.siam.sky.data.repo.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlertsViewModel(private val userRepo: UserRepo) : ViewModel() {

    private val _alertsState = MutableStateFlow<List<AlertModel>>(emptyList())
    val alertsState: StateFlow<List<AlertModel>> = _alertsState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepo.getAllAlerts().collect { alerts ->
                // Auto-delete alerts whose start time has already passed
                val now = System.currentTimeMillis()
                val started = alerts.filter { it.startTime <= now }
                started.forEach { userRepo.deleteAlert(it) }
                _alertsState.value = alerts.filter { it.startTime > now }
            }
        }
    }

    fun addAlert(alert: AlertModel) {
        viewModelScope.launch {
            userRepo.insertAlert(alert)
            userRepo.scheduleAlert(alert)
        }
    }

    fun removeAlert(alert: AlertModel) {
        viewModelScope.launch {
            userRepo.cancelAlert(alert)
            userRepo.deleteAlert(alert)
        }
    }

    fun toggleAlert(alert: AlertModel) {
        viewModelScope.launch {
            val updated = alert.copy(isActive = !alert.isActive)
            userRepo.updateAlert(updated)
            if (updated.isActive) {
                userRepo.scheduleAlert(updated)
            } else {
                userRepo.cancelAlert(updated)
            }
        }
    }

}