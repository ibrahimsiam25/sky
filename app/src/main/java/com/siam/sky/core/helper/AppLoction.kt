package com.siam.sky.core.helper

enum class AppLoction(val loction: String){
    GPS("gps"),
    MAP("map");

    companion object {
        fun fromLocation(location: String?): AppLoction {
            return when (location?.lowercase()) {
                GPS.loction -> GPS
                else -> MAP
            }
        }
    }
}