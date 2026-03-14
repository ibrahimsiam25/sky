package com.siam.sky.di

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.siam.sky.data.datasources.local.AlertLocalDataSource
import com.siam.sky.data.datasources.local.imp.AlertLocalDataSourceImp
import com.siam.sky.data.datasources.local.FavouriteLocalDataSource
import com.siam.sky.data.datasources.local.imp.FavouriteLocalDataSourceImp
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.datasources.local.imp.UserLocalDataSourceImp
import com.siam.sky.data.datasources.local.WeatherLocalDataSource
import com.siam.sky.data.datasources.local.imp.WeatherLocalDataSourceImp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataSourceModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences("sky_preferences", Context.MODE_PRIVATE)
    }

    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }

    single<WorkManager> {
        WorkManager.getInstance(androidContext())
    }

    factory<UserLocalDataSource> { UserLocalDataSourceImp(get(), get()) }
    factory<WeatherLocalDataSource> { WeatherLocalDataSourceImp(get()) }
    factory<FavouriteLocalDataSource> { FavouriteLocalDataSourceImp(get()) }
    factory<AlertLocalDataSource> { AlertLocalDataSourceImp(get(), get()) }
}
