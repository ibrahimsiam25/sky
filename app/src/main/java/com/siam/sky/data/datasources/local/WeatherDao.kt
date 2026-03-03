package com.siam.sky.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.siam.sky.data.models.WeatherModel

@Dao
interface WeatherDao{
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun  getWeather(weather: WeatherModel)


}