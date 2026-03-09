package com.siam.sky.presentaion.home.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.data.repo.WeatherRepo
import com.siam.sky.presentaion.home.view.HomePermissionHandler
import com.siam.sky.presentaion.home.viewmodel.HomeViewModel
import com.siam.sky.core.helper.AppUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.factory(UserRepo(UserLocalDataSource(context)),
            WeatherRepo(WeatherRemoteDataSource())
        )
    )
    val permissionHandler = remember(viewModel) { HomePermissionHandler(viewModel) }

    val location by viewModel.locationState.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()
    val hourlyState by viewModel.hourlyState.collectAsState()
    val dailyState by viewModel.dailyState.collectAsState()
    val unit by viewModel.unitState.collectAsState()

    permissionHandler.Bind()

    HomeScaffold(
        weatherState = weatherState,
        hourlyState = hourlyState,
        dailyState = dailyState,
        location = location,
        unit = unit
    )
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    HomeView()
}
