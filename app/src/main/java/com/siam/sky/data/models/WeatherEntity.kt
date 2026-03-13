package com.siam.sky.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val locationKey: String,
    val weatherJson: String = "",
    val hourlyJson: String = "",
    val dailyJson: String = "",
    val cachedAt: Long = System.currentTimeMillis()
)
