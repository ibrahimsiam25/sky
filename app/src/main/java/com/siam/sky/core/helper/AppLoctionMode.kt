package com.siam.sky.core.helper

enum class AppLoctionMode(val loction: String){
    GPS("gps"),
    MAP("map");

    companion object {
        fun fromLocation(location: String?): AppLoctionMode {
            return when (location?.lowercase()) {
                GPS.loction -> GPS
                else -> MAP
            }
        }
    }
}