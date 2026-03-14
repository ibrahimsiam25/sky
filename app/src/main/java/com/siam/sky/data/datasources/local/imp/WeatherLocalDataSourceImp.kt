package com.siam.sky.data.datasources.local.imp

import com.siam.sky.data.datasources.local.dao.WeatherDao
import com.siam.sky.data.datasources.local.WeatherLocalDataSource
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherEntity
import com.siam.sky.data.models.WeatherResponse

class WeatherLocalDataSourceImp(private val dao: WeatherDao) : WeatherLocalDataSource {

    private fun locationKey(lat: Double, lon: Double) = "%.6f_%.6f".format(lat, lon)

    override suspend fun saveCurrentWeather(lat: Double, lon: Double, weather: WeatherResponse) {
        val key = locationKey(lat, lon)
        val existing = dao.getByKey(key)
        dao.insert(
            WeatherEntity(
                locationKey = key,
                weatherResponse = weather,
                hourlyResponse = existing?.hourlyResponse,
                dailyResponse = existing?.dailyResponse
            )
        )
    }

    override suspend fun saveHourlyForecast(lat: Double, lon: Double, hourly: HourlyForecastResponse) {
        val key = locationKey(lat, lon)
        val existing = dao.getByKey(key)
        dao.insert(
            WeatherEntity(
                locationKey = key,
                weatherResponse = existing?.weatherResponse,
                hourlyResponse = hourly,
                dailyResponse = existing?.dailyResponse
            )
        )
    }

    override suspend fun saveDailyForecast(lat: Double, lon: Double, daily: DailyForecastResponse) {
        val key = locationKey(lat, lon)
        val existing = dao.getByKey(key)
        dao.insert(
            WeatherEntity(
                locationKey = key,
                weatherResponse = existing?.weatherResponse,
                hourlyResponse = existing?.hourlyResponse,
                dailyResponse = daily
            )
        )
    }

    override suspend fun getWeather(lat: Double, lon: Double): WeatherResponse? {
        val entity = dao.getByKey(locationKey(lat, lon)) ?: return null
        return entity.weatherResponse
    }

    override suspend fun getHourly(lat: Double, lon: Double): HourlyForecastResponse? {
        val entity = dao.getByKey(locationKey(lat, lon)) ?: return null
        return entity.hourlyResponse
    }

    override suspend fun getDaily(lat: Double, lon: Double): DailyForecastResponse? {
        val entity = dao.getByKey(locationKey(lat, lon)) ?: return null
        return entity.dailyResponse
    }
}
