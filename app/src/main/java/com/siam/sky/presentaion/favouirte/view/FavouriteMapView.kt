package com.siam.sky.presentaion.favouirte.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siam.sky.core.db.WeatherDataBase
import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.datasources.local.FavouriteLocalDataSource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.siam.sky.R
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.data.datasources.local.WeatherLocalDataSource
import com.siam.sky.data.repo.WeatherRepo
import com.siam.sky.presentaion.favouirte.viewmodel.FavouriteMapViewModel
import com.siam.sky.ui.theme.NavSurfaceBottom
import com.siam.sky.ui.theme.NavSurfaceTop
import com.siam.sky.ui.theme.WeekCardEnd
import com.siam.sky.ui.theme.WeekCardStart

@Composable
fun FavouriteMapView(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val database = WeatherDataBase.getInstance(context)
    val networkMonitor = NetworkMonitor(context)
    val viewModel: FavouriteMapViewModel = viewModel(
        factory = FavouriteMapViewModel.factory(
            WeatherRepo(
                WeatherRemoteDataSource(),
                WeatherLocalDataSource(database.getWeatherDao()),
                FavouriteLocalDataSource(database.getFavouriteLocationDao()),
                networkMonitor
            )
        )
    )

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchState by viewModel.searchState.collectAsState()
    val pickedLocation by viewModel.pickedLocation.collectAsState()
    val snapToLocation by viewModel.snapToLocation.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(pickedLocation.first, pickedLocation.second), 10f
        )
    }


    LaunchedEffect(snapToLocation) {
        snapToLocation?.let { (lat, lon) ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 12f),
                durationMs = 700
            )
        }
    }

    val markerState = MarkerState(position = LatLng(pickedLocation.first, pickedLocation.second))


    LaunchedEffect(pickedLocation) {
        markerState.position = LatLng(pickedLocation.first, pickedLocation.second)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                viewModel.onMapTap(latLng.latitude, latLng.longitude)
            }
        ) {
            Marker(
                state = markerState,
                draggable = true,
                onClick = { false }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(listOf(NavSurfaceTop, NavSurfaceBottom))
                )

                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.fav_map_screen_title),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))


                FavouriteMapSearchBar(
                    query = searchQuery,
                    searchState = searchState,
                    onQueryChange = { viewModel.onQueryChange(it) },
                    onCitySelected = { viewModel.onCitySelected(it) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(listOf(NavSurfaceTop, NavSurfaceBottom))
                )

                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = { viewModel.confirmSelection(onNavigateBack) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(0.dp, Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(WeekCardStart, WeekCardEnd)),
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.fav_btn_confirm),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
