package com.siam.sky.data.repo

import com.siam.sky.data.datasources.local.WeatherLocalDataSource
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherEntity
import com.siam.sky.data.models.WeatherResponse

class FakeWeatherDataSource(
    private val data: MutableMap<String, WeatherEntity> = mutableMapOf()
) : WeatherLocalDataSource {

    private fun key(lat: Double, lon: Double) = "%.6f_%.6f".format(lat, lon)

    override suspend fun saveCurrentWeather(
        lat: Double,
        lon: Double,
        weather: WeatherResponse
    ) {
        val k = key(lat, lon)
        val existing = data[k]

        data[k] = WeatherEntity(
            locationKey = k,
            weatherResponse = weather,
            hourlyResponse = existing?.hourlyResponse,
            dailyResponse = existing?.dailyResponse
        )
    }

    override suspend fun getWeather(
        lat: Double,
        lon: Double
    ): WeatherResponse? {
        return data[key(lat, lon)]?.weatherResponse
    }

    override suspend fun saveHourlyForecast(
        lat: Double,
        lon: Double,
        hourly: HourlyForecastResponse
    ) {
        val k = key(lat, lon)
        val existing = data[k]

        data[k] = WeatherEntity(
            locationKey = k,
            weatherResponse = existing?.weatherResponse,
            hourlyResponse = hourly,
            dailyResponse = existing?.dailyResponse
        )
    }

    override suspend fun getHourly(
        lat: Double,
        lon: Double
    ): HourlyForecastResponse? {
        return data[key(lat, lon)]?.hourlyResponse
    }

    override suspend fun saveDailyForecast(
        lat: Double,
        lon: Double,
        daily: DailyForecastResponse
    ) {
        val k = key(lat, lon)
        val existing = data[k]

        data[k] = WeatherEntity(
            locationKey = k,
            weatherResponse = existing?.weatherResponse,
            hourlyResponse = existing?.hourlyResponse,
            dailyResponse = daily
        )
    }

    override suspend fun getDaily(
        lat: Double,
        lon: Double
    ): DailyForecastResponse? {
        return data[key(lat, lon)]?.dailyResponse
    }
}