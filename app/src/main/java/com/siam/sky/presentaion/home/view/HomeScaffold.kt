package com.siam.sky.presentaion.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.siam.sky.R
import androidx.compose.ui.res.stringResource
import com.siam.sky.core.ApiState
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.WeatherResponse
import android.location.Location
import androidx.compose.foundation.layout.height

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    weatherState: ApiState<WeatherResponse>,
    hourlyState: ApiState<HourlyForecastResponse>,
    dailyState: ApiState<DailyForecastResponse>,
    location: Location?
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.homebackground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

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
                    weatherState = weatherState
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
                when (val state = weatherState) {
                    is ApiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                    is ApiState.Success -> WeatherHeroContent(state.data)
                    is ApiState.Error   -> {
                        Text(stringResource(R.string.error_prefix, state.message), color = Color.Red, modifier = Modifier.padding(top = 60.dp))
                    }
                    is ApiState.Idle    -> {
                        Text(
                            text = if (location == null) stringResource(R.string.fetching_location) else stringResource(R.string.loading_weather),
                            color = Color.White,
                            modifier = Modifier.padding(top = 60.dp)
                        )
                    }
                }
            }
        }
    }
}