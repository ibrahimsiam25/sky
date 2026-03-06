package com.siam.sky.data.models

data class HourlyForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<HourlyItem>,
    val city: ForecastCity
)

data class HourlyItem(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<WeatherItem>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val sys: HourlySys,
    val dt_txt: String
)

data class ForecastMain(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int?,
    val grnd_level: Int?,
    val humidity: Int,
    val temp_kf: Double?
)

data class HourlySys(
    val pod: String
)

data class ForecastCity(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)
