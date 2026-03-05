package com.siam.sky.data.repo

import com.siam.sky.data.datasources.local.WeatherLocalDataSource

class WeatherRepo(
    private val weatherLocalDataSource: WeatherLocalDataSource
) {
    // weather-related operations will go here
}