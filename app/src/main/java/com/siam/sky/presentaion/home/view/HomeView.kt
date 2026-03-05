package com.siam.sky.presentaion.home.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.siam.sky.R
import com.siam.sky.core.helper.LocationHelper
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.models.WeatherResponse
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.core.ApiState
import com.siam.sky.presentaion.home.viewmodel.HomeViewModel
import com.siam.sky.presentaion.home.viewmodel.PermissionStatus

@Composable
fun HomeView() {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.factory(
            UserRepo(UserLocalDataSource(context))
        )
    )

    val location by viewModel.locationState.collectAsState()
    val permissionStatus by viewModel.permissionStatus.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()

    val appSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (LocationHelper.checkPermission(context)) {
            viewModel.setPermissionStatus(PermissionStatus.GRANTED)
        }
    }

    val locationSettingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (LocationHelper.isLocationEnabled(context)) {
            viewModel.getFreshLocation()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.setPermissionStatus(PermissionStatus.GRANTED)
        } else {
            val activity = context as androidx.activity.ComponentActivity
            val permanentlyDenied =
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission.ACCESS_FINE_LOCATION
                ) &&
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    activity, Manifest.permission.ACCESS_COARSE_LOCATION
                )
            viewModel.setPermissionStatus(
                if (permanentlyDenied) PermissionStatus.PERMANENTLY_DENIED
                else PermissionStatus.DENIED
            )
        }
    }

    LaunchedEffect(Unit) {
        if (LocationHelper.checkPermission(context)) {
            viewModel.setPermissionStatus(PermissionStatus.GRANTED)
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(permissionStatus) {
        when (permissionStatus) {
            PermissionStatus.GRANTED -> {
                if (LocationHelper.isLocationEnabled(context)) {
                    viewModel.getFreshLocation()
                } else {
                    locationSettingsLauncher.launch(
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    )
                }
            }
            PermissionStatus.PERMANENTLY_DENIED -> {
                appSettingsLauncher.launch(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                )
            }
            PermissionStatus.DENIED -> {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            PermissionStatus.UNKNOWN -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets(0))
    ) {
        Image(
            painterResource(R.drawable.homebackground),
            contentDescription = "home bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            when (val state = weatherState) {
                is ApiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is ApiState.Success -> {
                    WeatherContent(weather = state.data)
                }
                is ApiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                is ApiState.Idle -> {
                    Text(
                        text = if (location == null) "Fetching location..." else "Loading weather...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherContent(weather: WeatherResponse) {
    val iconCode = weather.weather.firstOrNull()?.icon ?: "01d"
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // City & Country
        Text(
            text = "${weather.name}, ${weather.sys.country}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Weather icon
        GlideImage(
            model = iconUrl,
            contentDescription = "weather icon",
            modifier = Modifier.size(100.dp)
        )

        // Temperature
        Text(
            text = "${weather.main.temp.toInt()}Â°C",
            fontSize = 64.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        // Description
        Text(
            text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
            fontSize = 18.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Details row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(label = "Feels Like", value = "${weather.main.feels_like.toInt()}Â°C")
            WeatherDetailItem(label = "Humidity", value = "${weather.main.humidity}%")
            WeatherDetailItem(label = "Wind", value = "${weather.wind.speed} m/s")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(label = "Min Temp", value = "${weather.main.temp_min.toInt()}Â°C")
            WeatherDetailItem(label = "Max Temp", value = "${weather.main.temp_max.toInt()}Â°C")
            WeatherDetailItem(label = "Pressure", value = "${weather.main.pressure} hPa")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            WeatherDetailItem(label = "Visibility", value = "${weather.visibility / 1000} km")
            WeatherDetailItem(label = "Clouds", value = "${weather.clouds.all}%")
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}
