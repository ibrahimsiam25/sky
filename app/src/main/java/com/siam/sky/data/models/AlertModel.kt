package com.siam.sky.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "alerts_table")
data class AlertModel(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val startTime: Long,
    val endTime: Long,
    val type: AlertType,
    val isActive: Boolean = true
)

enum class AlertType {
    ALARM,
    NOTIFICATION
}