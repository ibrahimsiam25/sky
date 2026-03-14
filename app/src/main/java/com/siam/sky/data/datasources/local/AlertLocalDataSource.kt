package com.siam.sky.data.datasources.local

import com.siam.sky.data.models.AlertModel
import kotlinx.coroutines.flow.Flow

interface AlertLocalDataSource {
    fun scheduleAlert(alert: AlertModel)
    fun cancelAlert(alert: AlertModel)
    suspend fun insertAlert(alert: AlertModel)
    suspend fun deleteAlert(alert: AlertModel)
    suspend fun updateAlert(alert: AlertModel)
    fun getAllAlerts(): Flow<List<AlertModel>>
}
