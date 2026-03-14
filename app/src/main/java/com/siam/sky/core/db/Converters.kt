package com.siam.sky.core.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.siam.sky.data.models.AlertType
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse

class Converters {
    private val gson = Gson()

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

    @TypeConverter
    fun fromWeatherResponse(value: WeatherResponse?): String? {
        return if (value == null) null else gson.toJson(value)
    }

    @TypeConverter
    fun toWeatherResponse(value: String?): WeatherResponse? {
        return if (value.isNullOrEmpty()) null else try {
            gson.fromJson(value, WeatherResponse::class.java)
        } catch (e: Exception) { null }
    }

    @TypeConverter
    fun fromHourlyForecastResponse(value: HourlyForecastResponse?): String? {
        return if (value == null) null else gson.toJson(value)
    }

    @TypeConverter
    fun toHourlyForecastResponse(value: String?): HourlyForecastResponse? {
        return if (value.isNullOrEmpty()) null else try {
            gson.fromJson(value, HourlyForecastResponse::class.java)
        } catch (e: Exception) { null }
    }

    @TypeConverter
    fun fromDailyForecastResponse(value: DailyForecastResponse?): String? {
        return if (value == null) null else gson.toJson(value)
    }

    @TypeConverter
    fun toDailyForecastResponse(value: String?): DailyForecastResponse? {
        return if (value.isNullOrEmpty()) null else try {
            gson.fromJson(value, DailyForecastResponse::class.java)
        } catch (e: Exception) { null }
    }
}