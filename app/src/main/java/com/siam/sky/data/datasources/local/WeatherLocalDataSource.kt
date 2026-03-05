package com.siam.sky.data.datasources.local

import android.content.Context
import com.siam.sky.core.db.WeatherDataBase
import com.siam.sky.data.models.WeatherEntity

class WeatherLocalDataSource(val context: Context) {
    private var weatherDao: WeatherDao = WeatherDataBase.getInstance(context).getWeatherDao()

    suspend fun insertWeather(weather: WeatherEntity) = weatherDao.insertWeather(weather)

    suspend fun getAllWeather(): List<WeatherEntity> = weatherDao.getAllWeather()

    suspend fun deleteAllWeather() = weatherDao.deleteAllWeather()
}