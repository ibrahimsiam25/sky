package com.siam.sky.data.datasources.local.imp

import com.google.gson.Gson
import com.siam.sky.data.datasources.local.dao.WeatherDao
import com.siam.sky.data.datasources.local.WeatherLocalDataSource
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherEntity
import com.siam.sky.data.models.WeatherResponse

class WeatherLocalDataSourceImp(private val dao: WeatherDao) : WeatherLocalDataSource {

    private val gson = Gson()

    private fun locationKey(lat: Double, lon: Double) = "%.6f_%.6f".format(lat, lon)

    override suspend fun saveCurrentWeather(lat: Double, lon: Double, weather: WeatherResponse) {
        val key = locationKey(lat, lon)
        val existing = dao.getByKey(key)
        dao.insert(
            WeatherEntity(
                locationKey = key,
                weatherJson = gson.toJson(weather),
                hourlyJson = existing?.hourlyJson ?: "",
                dailyJson = existing?.dailyJson ?: ""
            )
        )
    }

    override suspend fun saveHourlyForecast(lat: Double, lon: Double, hourly: HourlyForecastResponse) {
        val key = locationKey(lat, lon)
        val existing = dao.getByKey(key)
        dao.insert(
            WeatherEntity(
                locationKey = key,
                weatherJson = existing?.weatherJson ?: "",
                hourlyJson = gson.toJson(hourly),
                dailyJson = existing?.dailyJson ?: ""
            )
        )
    }

    override suspend fun saveDailyForecast(lat: Double, lon: Double, daily: DailyForecastResponse) {
        val key = locationKey(lat, lon)
        val existing = dao.getByKey(key)
        dao.insert(
            WeatherEntity(
                locationKey = key,
                weatherJson = existing?.weatherJson ?: "",
                hourlyJson = existing?.hourlyJson ?: "",
                dailyJson = gson.toJson(daily)
            )
        )
    }

    override suspend fun getWeather(lat: Double, lon: Double): WeatherResponse? {
        val entity = dao.getByKey(locationKey(lat, lon)) ?: return null
        if (entity.weatherJson.isEmpty()) return null
        return try {
            gson.fromJson(entity.weatherJson, WeatherResponse::class.java)
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getHourly(lat: Double, lon: Double): HourlyForecastResponse? {
        val entity = dao.getByKey(locationKey(lat, lon)) ?: return null
        if (entity.hourlyJson.isEmpty()) return null
        return try {
            gson.fromJson(entity.hourlyJson, HourlyForecastResponse::class.java)
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getDaily(lat: Double, lon: Double): DailyForecastResponse? {
        val entity = dao.getByKey(locationKey(lat, lon)) ?: return null
        if (entity.dailyJson.isEmpty()) return null
        return try {
            gson.fromJson(entity.dailyJson, DailyForecastResponse::class.java)
        } catch (_: Exception) {
            null
        }
    }
}
