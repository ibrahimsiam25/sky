package com.siam.sky.presentaion.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.siam.sky.R
import androidx.compose.ui.res.stringResource
import com.siam.sky.core.ApiState
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import android.location.Location
import androidx.compose.foundation.layout.height
import com.siam.sky.core.common.AppErrorView
import com.siam.sky.core.common.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    weatherState: ApiState<WeatherResponse>,
    hourlyState: ApiState<HourlyForecastResponse>,
    dailyState: ApiState<DailyForecastResponse>,
    location: Location?,
    unit: AppUnit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val isAllContentReady = weatherState is ApiState.Success &&
            hourlyState is ApiState.Success &&
            dailyState is ApiState.Success
    val errorMessage = when {
        weatherState is ApiState.Error -> weatherState.message
        hourlyState is ApiState.Error -> hourlyState.message
        dailyState is ApiState.Error -> dailyState.message
        else -> null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Background()

        when {
            errorMessage != null -> {
                AppErrorView(
                    title = stringResource(R.string.home_error_title),
                    message = errorMessage,
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                )
            }

            isAllContentReady -> {
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = 280.dp,
                    sheetShape = RoundedCornerShape(topStart = 44.dp, topEnd = 44.dp),
                    sheetContainerColor = Color.Transparent,
                    sheetShadowElevation = 0.dp,
                    sheetTonalElevation = 0.dp,
                    sheetDragHandle = {
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 6.dp)
                                .width(48.dp)
                                .height(5.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.Black.copy(alpha = 0.30f))
                        )
                    },
                    sheetContent = {
                        BottomSheetContent(
                            hourlyState = hourlyState,
                            dailyState = dailyState,
                            weatherState = weatherState,
                            unit = unit
                        )
                    },
                    containerColor = Color.Transparent
                ) { _ ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        WeatherHeroContent((weatherState as ApiState.Success).data, unit)
                    }
                }
            }

            else -> {
                HomeLoadingShimmer(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                )
            }
        }
    }
}