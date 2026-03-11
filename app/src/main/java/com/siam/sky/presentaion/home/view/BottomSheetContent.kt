package com.siam.sky.presentaion.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.siam.sky.R
import com.siam.sky.core.ApiState
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.ui.theme.NavSurfaceTop
import com.siam.sky.ui.theme.NavSurfaceBottom
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse

@Composable
fun BottomSheetContent(
    hourlyState: ApiState<HourlyForecastResponse>,
    dailyState: ApiState<DailyForecastResponse>,
    weatherState: ApiState<WeatherResponse>,
    unit: AppUnit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NavSurfaceTop.copy(alpha = 0.92f),
                        NavSurfaceBottom.copy(alpha = 0.96f)
                    )
                ),
                shape = RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp)
            )
            .verticalScroll(rememberScrollState())
            .padding(bottom = 40.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(49.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                ForecastTab(
                    title = stringResource(R.string.tab_hourly_forecast),
                    selected = selectedTab == 0
                ) { selectedTab = 0 }

                ForecastTab(
                    title = stringResource(R.string.tab_weekly_forecast),
                    selected = selectedTab == 1
                ) { selectedTab = 1 }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.30f))
                    .align(Alignment.BottomCenter)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            HourlySection(hourlyState, unit)
            Spacer(modifier = Modifier.height(24.dp))
            if (weatherState is ApiState.Success) {
                DayDetailSection(weather = weatherState.data, unit = unit)
            }
        } else {
            WeeklySection(dailyState, weatherState, unit)
        }
    }
}
@Composable
fun ForecastTab(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier
            .clickable { onClick() }
            .padding(bottom = 6.dp),
        text = title,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.5).sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = if (selected) Color.White else Color.White.copy(alpha = 0.60f)
    )
}