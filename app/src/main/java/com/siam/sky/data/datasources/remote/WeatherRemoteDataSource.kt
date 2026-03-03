package com.siam.sky.data.datasources.remote

import com.siam.sky.core.network.RetrofitHelper


class WeatherRemoteDataSource {
    val weatherService: WeatherService = RetrofitHelper.weatherService

 //   suspend fun getProducts() = weatherService.getProducts().products
}