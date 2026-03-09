package com.siam.sky.core.helper

enum class AppUnit(val unit: String) {
    STANDARD("standard"),
    METRIC("metric"),
    IMPERIAL("imperial");


    companion object {
       fun  fromUnit(unit: String?): AppUnit {
            return when (unit?.lowercase()) {
                "metric" -> METRIC
                "imperial" -> IMPERIAL
                else -> STANDARD
            }
        }
    }
}