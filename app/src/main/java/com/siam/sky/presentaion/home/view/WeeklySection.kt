package com.siam.sky.presentaion.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.siam.sky.R
import com.siam.sky.core.ApiState
import com.siam.sky.core.helper.forecastDrawableForWeather
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.DailyItem
import com.siam.sky.data.models.WeatherResponse
import com.siam.sky.ui.theme.WhiteFaded
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



@Composable
fun WeeklySection(
    state: ApiState<DailyForecastResponse>,
    weatherState: ApiState<WeatherResponse>
) {
    val cityName = if (weatherState is ApiState.Success)
        "${weatherState.data.name}, ${weatherState.data.sys.country}" else ""

    when (state) {
        is ApiState.Loading -> Box(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = Color.White, modifier = Modifier.size(28.dp)) }

        is ApiState.Success -> Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            state.data.list.forEach { item ->
                DailyForecastCard(item = item)
            }
        }

        is ApiState.Error -> Text(
            text = stringResource(R.string.weekly_unavailable),
            color = WhiteFaded,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        is ApiState.Idle -> Unit
    }
}

@Composable
fun DailyForecastCard(item: DailyItem) {
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0] ?: Locale.getDefault()
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val dayName = remember(item.dt) {
        SimpleDateFormat("EEEE, MMM dd", locale).format(Date(item.dt * 1000L))
    }
    val iconCode = item.weather.firstOrNull()?.icon ?: "01d"
    val cardDrawable = forecastDrawableForWeather(
        iconCode = iconCode,
    )
    val description = item.weather.firstOrNull()?.description
        ?.replaceFirstChar { it.uppercase() } ?: ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.weathercard),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = if (isRtl) -1f else 1f
                }
        )



        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 14.dp, bottom = 14.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top
            ) {

                Text(
                    text = "${item.temp.day.toInt()}°",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.374.sp,
                    color = Color.White,
                    lineHeight = 44.sp
                )
                Text(
                    text = dayName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )


                    Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = description,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.90f)
                        )
                        Text(
                            text = stringResource(R.string.high_low, item.temp.max.toInt(), item.temp.min.toInt()),
                            fontSize = 12.sp,
                            color = WhiteFaded
                        )




            }

            Image(
                painter = painterResource(cardDrawable),
                contentDescription = null,
                modifier = Modifier.height(160.dp).width(160.dp).offset(10.dp, -30.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
