package com.siam.sky.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_locations")
data class FavouriteLocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val lat: Double,
    val lon: Double,
    val addedAt: Long = System.currentTimeMillis()
)
