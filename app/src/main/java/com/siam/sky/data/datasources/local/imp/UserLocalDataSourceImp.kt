package com.siam.sky.data.datasources.local.imp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.location.Location
import androidx.core.content.edit
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.data.datasources.local.UserLocalDataSource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserLocalDataSourceImp(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val sharedPreferences: SharedPreferences
) : UserLocalDataSource {

    @SuppressLint("MissingPermission")
    override fun getFreshLocation(): Flow<Location> = callbackFlow {
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

    override fun observeAppLanguage(): Flow<AppLanguage> = callbackFlow {
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

    override fun observeUnit(): Flow<AppUnit> = callbackFlow {
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

    override fun observeLastKnownLocation(): Flow<Pair<Float, Float>> = callbackFlow {
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

    override fun observeLocationMode(): Flow<AppLoctionMode> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_LOCATION_MODE) trySend(getSavedLocationMode())
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun saveLocationMode(mode: AppLoctionMode) {
        sharedPreferences.edit { putString(KEY_LOCATION_MODE, mode.loction) }
    }

    override fun getSavedLocationMode(): AppLoctionMode {
        return AppLoctionMode.fromLocation(
            sharedPreferences.getString(KEY_LOCATION_MODE, AppLoctionMode.GPS.loction)
        )
    }

    override fun updateAppLanguage(language: AppLanguage) {
        sharedPreferences.edit {
            putString(KEY_APP_LANGUAGE, language.localeTag)
        }
    }

    override fun updateUnit(unit: AppUnit) {
        sharedPreferences.edit {
            putString(KEY_UINT, unit.unit)
        }
    }

    override fun updateLastKnownLocation(lat: Float, lon: Float) {
        sharedPreferences.edit {
            putFloat(KEY_LAST_KNOWN_LOCATION_LAT, lat)
            putFloat(KEY_LAST_KNOWN_LOCATION_LON, lon)
        }
    }

    override fun getSavedAppLanguage(): AppLanguage {
        return AppLanguage.fromLocaleTag(
            sharedPreferences.getString(KEY_APP_LANGUAGE, AppLanguage.ENGLISH.localeTag)
        )
    }

    override fun getSavedAppUnit(): AppUnit {
        return AppUnit.fromUnit(
            sharedPreferences.getString(KEY_UINT, AppUnit.METRIC.unit) ?: AppUnit.METRIC.unit
        )
    }

    override fun getLastKnownLocation(): Pair<Float, Float> {
        val lat = sharedPreferences.getFloat(KEY_LAST_KNOWN_LOCATION_LAT, 0f)
        val lon = sharedPreferences.getFloat(KEY_LAST_KNOWN_LOCATION_LON,0f)
        return Pair(lat, lon)
    }

    private companion object {
        const val KEY_APP_LANGUAGE = "app_language"
        const val KEY_UINT = "sky_key_unit"
        const val KEY_LAST_KNOWN_LOCATION_LAT = "last_known_location_lat"
        const val KEY_LAST_KNOWN_LOCATION_LON = "last_known_location_lon"
        const val KEY_LOCATION_MODE = "location_mode"
    }
}
