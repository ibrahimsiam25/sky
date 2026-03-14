package com.siam.sky.data.datasources.local

import android.location.Location
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.core.helper.AppUnit
import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
    fun getFreshLocation(): Flow<Location>
    fun observeAppLanguage(): Flow<AppLanguage>
    fun observeUnit(): Flow<AppUnit>
    fun observeLastKnownLocation(): Flow<Pair<Float, Float>>
    fun observeLocationMode(): Flow<AppLoctionMode>
    fun saveLocationMode(mode: AppLoctionMode)
    fun getSavedLocationMode(): AppLoctionMode
    fun updateAppLanguage(language: AppLanguage)
    fun updateUnit(unit: AppUnit)
    fun updateLastKnownLocation(lat: Float, lon: Float)
    fun getSavedAppLanguage(): AppLanguage
    fun getSavedAppUnit(): AppUnit
    fun getLastKnownLocation(): Pair<Float, Float>
}
