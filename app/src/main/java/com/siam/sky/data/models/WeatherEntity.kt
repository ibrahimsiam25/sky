package com.siam.sky.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityName: String,
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val weatherDescription: String,
    val weatherIcon: String,
    val country: String,
    val sunrise: Long,
    val sunset: Long,
    val visibility: Int,
    val dt: Long
)

