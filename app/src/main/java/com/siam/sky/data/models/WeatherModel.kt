package com.siam.sky.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName =  "weather")
data class WeatherModel (
    @PrimaryKey
    val name: String,
)