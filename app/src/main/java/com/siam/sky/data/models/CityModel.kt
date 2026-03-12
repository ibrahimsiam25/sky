package com.siam.sky.data.models

typealias CityResponse = List<CityModel>

data class CityModel(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)