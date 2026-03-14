package com.siam.sky.di

import com.siam.sky.core.db.WeatherDataBase
import com.siam.sky.data.datasources.local.dao.AlertDao
import com.siam.sky.data.datasources.local.dao.FavouriteLocationDao
import com.siam.sky.data.datasources.local.dao.WeatherDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<WeatherDataBase> { WeatherDataBase.getInstance(androidContext()) }
    single<WeatherDao> { get<WeatherDataBase>().getWeatherDao() }
    single<FavouriteLocationDao> { get<WeatherDataBase>().getFavouriteLocationDao() }
    single<AlertDao> { get<WeatherDataBase>().getAlertDao() }
}
