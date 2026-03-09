package com.siam.sky.data.datasources.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.siam.sky.core.helper.AppLanguage
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

    private companion object {
        const val PREFERENCES_NAME = "sky_preferences"
        const val KEY_APP_LANGUAGE = "app_language"

        const val KEY_UINT = "sky_key_unit"
    }
}