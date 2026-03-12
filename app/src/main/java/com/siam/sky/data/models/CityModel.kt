package com.siam.sky.data.models

data class CityResponse(

    val data: List<CityModel>

    )


data class CityModel(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String
)