package com.siam.sky.data.models

data class DailyForecastResponse(
    val city: DailyCity,
    val cod: String,
    val message: Double,
    val cnt: Int,
    val list: List<DailyItem>
)

data class DailyCity(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int
)

data class DailyItem(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: DailyTemp,
    val feels_like: DailyFeelsLike,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherItem>,
    val speed: Double,
    val deg: Int,
    val gust: Double?,
    val clouds: Int,
    val pop: Double
)

data class DailyTemp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class DailyFeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)
