package com.siam.sky.presentaion.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.siam.sky.R
import androidx.compose.ui.res.stringResource
import com.siam.sky.core.ResponseState
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import android.location.Location
import com.siam.sky.core.common.AppErrorView
import com.siam.sky.core.common.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    weatherState: ResponseState<WeatherResponse>,
    hourlyState: ResponseState<HourlyForecastResponse>,
    dailyState: ResponseState<DailyForecastResponse>,
    location: Location?,
    unit: AppUnit,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {}
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val isAllContentReady = weatherState is ResponseState.Success &&
            hourlyState is ResponseState.Success &&
            dailyState is ResponseState.Success
    val errorMessage = when {
        weatherState is ResponseState.Error -> weatherState.message
        hourlyState is ResponseState.Error -> hourlyState.message
        dailyState is ResponseState.Error -> dailyState.message
        else -> null
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Background()

        BoxWithConstraints(Modifier.fillMaxSize()) {
            val screenHeight = maxHeight
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight)
                ) {
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
                        WeatherHeroContent((weatherState as ResponseState.Success).data, unit)
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
        }
    }
}