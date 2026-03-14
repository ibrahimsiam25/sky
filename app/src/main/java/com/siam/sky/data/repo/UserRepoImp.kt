package com.siam.sky.data.repo

import android.location.Location
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.data.datasources.local.AlertLocalDataSource
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.models.AlertModel
import kotlinx.coroutines.flow.Flow

class UserRepoImp(
    private val userLocalDataSource: UserLocalDataSource,
    private val alertLocalDataSource: AlertLocalDataSource
) : UserRepo {

    override fun getFreshLocation(): Flow<Location> = userLocalDataSource.getFreshLocation()

    override fun observeAppLanguage(): Flow<AppLanguage> = userLocalDataSource.observeAppLanguage()

    override fun updateAppLanguage(language: AppLanguage) {
        userLocalDataSource.updateAppLanguage(language)
    }

    override fun getSavedAppLanguage(): AppLanguage = userLocalDataSource.getSavedAppLanguage()

    override fun observeUnit(): Flow<AppUnit> = userLocalDataSource.observeUnit()

    override fun getSavedAppUnit(): AppUnit = userLocalDataSource.getSavedAppUnit()

    override fun updateAppUnit(unit: AppUnit) {
        userLocalDataSource.updateUnit(unit)
    }

    override fun observeLastKnownLocation(): Flow<Pair<Float, Float>> =
        userLocalDataSource.observeLastKnownLocation()

    override fun getSavedLocationSource(): Pair<Float, Float> = userLocalDataSource.getLastKnownLocation()

    override fun updateLocationSource(lat: Float, lon: Float) {
        userLocalDataSource.updateLastKnownLocation(lat, lon)
    }

    override fun getSavedLocationMode(): AppLoctionMode = userLocalDataSource.getSavedLocationMode()

    override fun saveLocationMode(mode: AppLoctionMode) {
        userLocalDataSource.saveLocationMode(mode)
    }

    override fun observeLocationMode(): Flow<AppLoctionMode> = userLocalDataSource.observeLocationMode()

    override fun scheduleAlert(alert: AlertModel) {
        alertLocalDataSource.scheduleAlert(alert)
    }

    override fun cancelAlert(alert: AlertModel) {
        alertLocalDataSource.cancelAlert(alert)
    }

    override suspend fun insertAlert(alert: AlertModel) {
        alertLocalDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        alertLocalDataSource.deleteAlert(alert)
    }

    override suspend fun updateAlert(alert: AlertModel) {
        alertLocalDataSource.updateAlert(alert)
    }

    override fun getAllAlerts(): Flow<List<AlertModel>> = alertLocalDataSource.getAllAlerts()
}
