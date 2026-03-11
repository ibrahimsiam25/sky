package com.siam.sky.data.datasources.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoction
import com.siam.sky.core.helper.AppUnit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import androidx.core.content.edit

class UserLocalDataSource(private val context: Context) {

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
    fun observeLocationMode(): Flow<AppLoction> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_LOCATION_MODE) trySend(getSavedLocationMode())
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    fun saveLocationMode(mode: AppLoction) {
        sharedPreferences.edit { putString(KEY_LOCATION_MODE, mode.loction) }
    }

    fun getSavedLocationMode(): AppLoction {
        return AppLoction.fromLocation(
            sharedPreferences.getString(KEY_LOCATION_MODE, AppLoction.GPS.loction)
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
    fun udateLastKnownLocation(lat: Float, lon: Float) {
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


    private companion object {
        const val PREFERENCES_NAME = "sky_preferences"
        const val KEY_APP_LANGUAGE = "app_language"

        const val KEY_UINT = "sky_key_unit"

        const val KEY_LAST_KNOWN_LOCATION_LAT = "last_known_location_lat"
        const val KEY_LAST_KNOWN_LOCATION_LON = "last_known_location_lon"
        const val KEY_LOCATION_MODE = "location_mode"
    }
}