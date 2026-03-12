package com.siam.sky.core.db

import androidx.room.TypeConverter
import com.siam.sky.data.models.AlertType

class Converters {
    @TypeConverter
    fun fromAlertType(value: AlertType): String {
        return value.name
    }

    @TypeConverter
    fun toAlertType(value: String): AlertType {
        return try {
            AlertType.valueOf(value)
        } catch (e: Exception) {
            AlertType.NOTIFICATION
        }
    }
}