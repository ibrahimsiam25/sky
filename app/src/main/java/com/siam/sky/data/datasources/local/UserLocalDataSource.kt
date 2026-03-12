package com.siam.sky.data.datasources.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.core.helper.AppUnit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import androidx.core.content.edit
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.siam.sky.data.models.AlertModel
import com.siam.sky.data.models.AlertType
import com.siam.sky.data.services.StopAlertWorker
import com.siam.sky.data.services.WeatherAlertWorker
import java.util.concurrent.TimeUnit

class UserLocalDataSource(
    private val context: Context,
    private val alertDao: AlertDao? = null
) {

    private val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    @SuppressLint("MissingPermission")
    fun getFreshLocation(): Flow<Location> = callbackFlow {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->
            location?.let { trySend(it) }
            close()
        }.addOnFailureListener { e ->
            close(e)
        }
        awaitClose()
    }

    fun observeAppLanguage(): Flow<AppLanguage> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_APP_LANGUAGE) {
                trySend(getSavedAppLanguage())
            }
        }


        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    fun observeUnit(): Flow<AppUnit> =callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_UINT) {
                trySend(getSavedAppUnit())
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }

    }
    fun observeLastKnownLocation(): Flow<Pair<Float, Float>> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_LAST_KNOWN_LOCATION_LAT || key == KEY_LAST_KNOWN_LOCATION_LON) {
                trySend(getLastKnownLocation())
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
    fun observeLocationMode(): Flow<AppLoctionMode> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_LOCATION_MODE) trySend(getSavedLocationMode())
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun saveLocationMode(mode: AppLoctionMode) {
        sharedPreferences.edit { putString(KEY_LOCATION_MODE, mode.loction) }
    }

    fun getSavedLocationMode(): AppLoctionMode {
        return AppLoctionMode.fromLocation(
            sharedPreferences.getString(KEY_LOCATION_MODE, AppLoctionMode.GPS.loction)
        )
    }

    fun updateAppLanguage(language: AppLanguage) {
        sharedPreferences.edit {
            putString(KEY_APP_LANGUAGE, language.localeTag)
        }
    }

    fun updateUnit(unit: AppUnit) {
        sharedPreferences.edit {
            putString(KEY_UINT, unit.unit)
        }
    }
    fun updateLastKnownLocation(lat: Float, lon: Float) {
        sharedPreferences.edit {
            putFloat(KEY_LAST_KNOWN_LOCATION_LAT, lat)
            putFloat(KEY_LAST_KNOWN_LOCATION_LON, lon)
        }
    }
    fun getSavedAppLanguage(): AppLanguage {
        return AppLanguage.fromLocaleTag(
            sharedPreferences.getString(KEY_APP_LANGUAGE, AppLanguage.ENGLISH.localeTag)
        )
    }

    fun getSavedAppUnit(): AppUnit {
       return AppUnit.fromUnit(
            sharedPreferences.getString(KEY_UINT, AppUnit.METRIC.unit) ?: AppUnit.METRIC.unit
        )

    }

    fun getLastKnownLocation(): Pair<Float, Float> {
        val lat = sharedPreferences.getFloat(KEY_LAST_KNOWN_LOCATION_LAT, 30.05533f)
        val lon = sharedPreferences.getFloat(KEY_LAST_KNOWN_LOCATION_LON, 31.2031467f)
        return Pair(lat, lon)
    }

    fun scheduleAlert(alert: AlertModel) {
        val currentTime = System.currentTimeMillis()
        val startDelay = (alert.startTime - currentTime).coerceAtLeast(0L)
        val endDelay = (alert.endTime - currentTime).coerceAtLeast(0L)
        val tag = "alert_${alert.id}"

        val startWork = OneTimeWorkRequestBuilder<WeatherAlertWorker>()
            .setInitialDelay(startDelay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("ALERT_TYPE" to alert.type.name, "ALERT_ID" to alert.id))
            .addTag(tag)
            .build()

        val stopWork = OneTimeWorkRequestBuilder<StopAlertWorker>()
            .setInitialDelay(endDelay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("ALERT_ID" to alert.id))
            .addTag(tag)
            .build()

        WorkManager.getInstance(context).enqueue(listOf(startWork, stopWork))
    }

    fun cancelAlert(alert: AlertModel) {
        WorkManager.getInstance(context).cancelAllWorkByTag("alert_${alert.id}")
    }

    suspend fun insertAlert(alert: AlertModel) {
        alertDao?.insertAlert(alert)
    }

    suspend fun deleteAlert(alert: AlertModel) {
        alertDao?.deleteAlert(alert)
    }

    suspend fun updateAlert(alert: AlertModel) {
        alertDao?.updateAlert(alert)
    }

    fun getAllAlerts(): Flow<List<AlertModel>> {
        return alertDao?.getAllAlerts() ?: kotlinx.coroutines.flow.flowOf(emptyList())
    }
    private companion object {
        const val PREFERENCES_NAME = "sky_preferences"
        const val KEY_APP_LANGUAGE = "app_language"

        const val KEY_UINT = "sky_key_unit"

        const val KEY_LAST_KNOWN_LOCATION_LAT = "last_known_location_lat"
        const val KEY_LAST_KNOWN_LOCATION_LON = "last_known_location_lon"
        const val KEY_LOCATION_MODE = "location_mode"
    }
}