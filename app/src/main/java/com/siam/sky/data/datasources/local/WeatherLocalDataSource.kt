package com.siam.sky.data.datasources.local

import android.content.Context
import com.siam.sky.core.db.WeatherDataBase

class WeatherLocalDataSource(val context: Context){
    var weatherDao: WeatherDao = WeatherDataBase.getInstance(context).getWeatherDao()

    suspend fun getFavProducts() = weatherDao.getWeather(
        weather = TODO()
    )
}