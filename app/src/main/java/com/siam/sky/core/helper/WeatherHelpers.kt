package com.siam.sky.core.helper

import com.siam.sky.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun forecastDrawableForWeather(iconCode: String,): Int {
    return when {
        iconCode == "01d"                                                      -> R.drawable.sun_cloud_angled_rain
        iconCode == "01n"                                                      -> R.drawable.moon_cloud_fast_wind
        iconCode.startsWith("11")                                      -> R.drawable.tornado
        iconCode.startsWith("13")                                      -> R.drawable.moon_cloud_mid_rain
        iconCode.startsWith("50")                                      -> R.drawable.moon_cloud_fast_wind
        iconCode.startsWith("09") || iconCode == "10d"                 -> R.drawable.sun_cloud_angled_rain
        iconCode == "10n"                                                      -> R.drawable.moon_cloud_mid_rain
        iconCode.startsWith("02") || iconCode == "03d"                 -> R.drawable.sun_cloud_angled_rain
        iconCode.startsWith("03") || iconCode.startsWith("04") -> R.drawable.moon_cloud_fast_wind
        else                                                                   -> R.drawable.sun_cloud_angled_rain
    }
}

fun timestampToTime(ts: Long): String =
    SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(ts * 1000L))
