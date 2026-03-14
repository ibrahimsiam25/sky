package com.siam.sky.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val locationKey: String,
    val weatherResponse: WeatherResponse? = null,
    val hourlyResponse: HourlyForecastResponse? = null,
    val dailyResponse: DailyForecastResponse? = null,
    val cachedAt: Long = System.currentTimeMillis()
)
