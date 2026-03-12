package com.siam.sky.data.datasources.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.siam.sky.data.models.AlertModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: AlertModel)

    @Delete
    suspend fun deleteAlert(alert: AlertModel)

    @Update
    suspend fun updateAlert(alert: AlertModel)

    @Query("DELETE FROM alerts_table WHERE id = :id")
    suspend fun deleteAlertById(id: String)

    @Query("SELECT * FROM alerts_table ORDER BY startTime DESC")
    fun getAllAlerts(): Flow<List<AlertModel>>
}