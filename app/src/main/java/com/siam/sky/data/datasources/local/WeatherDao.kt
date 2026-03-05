package com.siam.sky.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.siam.sky.data.models.WeatherEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_table ORDER BY id DESC")
    suspend fun getAllWeather(): List<WeatherEntity>

    @Query("DELETE FROM weather_table")
    suspend fun deleteAllWeather()
}