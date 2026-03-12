package com.siam.sky.data.repo

import android.location.Location
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.models.AlertModel
import kotlinx.coroutines.flow.Flow

class UserRepo(private val userLocalDataSource: UserLocalDataSource) {

    fun getFreshLocation(): Flow<Location> {
        return userLocalDataSource.getFreshLocation()
    }

    fun observeAppLanguage(): Flow<AppLanguage> {
        return userLocalDataSource.observeAppLanguage()
    }

    fun updateAppLanguage(language: AppLanguage) {
        userLocalDataSource.updateAppLanguage(language)
    }

    fun getSavedAppLanguage(): AppLanguage {
        return userLocalDataSource.getSavedAppLanguage()
    }

    fun observeUnit(): Flow<AppUnit> {
        return userLocalDataSource.observeUnit()
    }

    fun getSavedAppUnit(): AppUnit {
        return userLocalDataSource.getSavedAppUnit()
    }

    fun updateAppUnit(unit: AppUnit) {
        userLocalDataSource.updateUnit(unit)
    }

    fun observeLastKnownLocation(): Flow<Pair<Float, Float>>  {
        return userLocalDataSource.observeLastKnownLocation()
    }

     fun getSavedLocationSource(): Pair<Float, Float>  {
        return userLocalDataSource.getLastKnownLocation()
    }

     fun updateLocationSource(lat: Float, lon: Float) {
        userLocalDataSource.updateLastKnownLocation(lat, lon)
    }

    fun getSavedLocationMode(): AppLoctionMode = userLocalDataSource.getSavedLocationMode()
    fun saveLocationMode(mode: AppLoctionMode) = userLocalDataSource.saveLocationMode(mode)
    fun observeLocationMode(): Flow<AppLoctionMode> = userLocalDataSource.observeLocationMode()

    // Alerts
    fun scheduleAlert(alert: AlertModel) {
        userLocalDataSource.scheduleAlert(alert)
    }

    fun cancelAlert(alert: AlertModel) {
        userLocalDataSource.cancelAlert(alert)
    }

    suspend fun insertAlert(alert: AlertModel) {
        userLocalDataSource.insertAlert(alert)
    }

    suspend fun deleteAlert(alert: AlertModel) {
        userLocalDataSource.deleteAlert(alert)
    }

    suspend fun updateAlert(alert: AlertModel) {
        userLocalDataSource.updateAlert(alert)
    }

    fun getAllAlerts(): Flow<List<AlertModel>> {
        return userLocalDataSource.getAllAlerts()
    }
}
