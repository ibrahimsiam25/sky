package com.siam.sky.presentaion.home.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import com.siam.sky.presentaion.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = koinViewModel()
    val permissionHandler = remember(viewModel) { HomePermissionHandler(viewModel) }

    val location by viewModel.locationState.collectAsState()
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

    permissionHandler.Bind()

    HomeScaffold(
        weatherState = weatherState,
        hourlyState = hourlyState,
        dailyState = dailyState,
        location = location,
        unit = unit,
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    HomeView()
}
