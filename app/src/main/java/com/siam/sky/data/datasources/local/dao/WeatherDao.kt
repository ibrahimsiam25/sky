package com.siam.sky.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.siam.sky.data.models.WeatherEntity

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WeatherEntity)

    @Query("SELECT * FROM weather WHERE locationKey = :key LIMIT 1")
    suspend fun getByKey(key: String): WeatherEntity?
}
