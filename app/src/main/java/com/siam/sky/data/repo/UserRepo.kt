package com.siam.sky.data.repo

import android.location.Location
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.data.models.AlertModel
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getFreshLocation(): Flow<Location>
    fun observeAppLanguage(): Flow<AppLanguage>
    fun updateAppLanguage(language: AppLanguage)
    fun getSavedAppLanguage(): AppLanguage
    fun observeUnit(): Flow<AppUnit>
    fun getSavedAppUnit(): AppUnit
    fun updateAppUnit(unit: AppUnit)
    fun observeLastKnownLocation(): Flow<Pair<Float, Float>>
    fun getSavedLocationSource(): Pair<Float, Float>
    fun updateLocationSource(lat: Float, lon: Float)
    fun getSavedLocationMode(): AppLoctionMode
    fun saveLocationMode(mode: AppLoctionMode)
    fun observeLocationMode(): Flow<AppLoctionMode>
    fun scheduleAlert(alert: AlertModel)
    fun cancelAlert(alert: AlertModel)
    suspend fun insertAlert(alert: AlertModel)
    suspend fun deleteAlert(alert: AlertModel)
    suspend fun updateAlert(alert: AlertModel)
    fun getAllAlerts(): Flow<List<AlertModel>>
}
