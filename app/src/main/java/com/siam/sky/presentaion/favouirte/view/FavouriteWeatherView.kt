package com.siam.sky.presentaion.favouirte.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.siam.sky.R
import com.siam.sky.presentaion.favouirte.viewmodel.FavouriteWeatherViewModel
import com.siam.sky.presentaion.home.view.HomeScaffold
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FavouriteWeatherView(
    lat: Double,
    lon: Double,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: FavouriteWeatherViewModel = koinViewModel(
        parameters = { parametersOf(lat, lon) }
    )

    val weatherState by viewModel.weatherState.collectAsState()
    val hourlyState by viewModel.hourlyState.collectAsState()
    val dailyState by viewModel.dailyState.collectAsState()
    val unit by viewModel.unitState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.toastEvent.collectLatest { resId ->
            Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show()
        }
    }

    Box {
        HomeScaffold(
            weatherState = weatherState,
            hourlyState = hourlyState,
            dailyState = dailyState,
            location = null,
            unit = unit,
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refresh() }
        )

        Surface(
            modifier = Modifier
                .safeDrawingPadding()
                .padding(start = 12.dp, top = 10.dp),
            color = Color.Black.copy(alpha = 0.28f),
            shape = RoundedCornerShape(14.dp)
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.background(Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White
                )
            }
        }
    }
}
