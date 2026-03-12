package com.siam.sky.presentaion.settings.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.siam.sky.R
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.presentaion.settings.viewmodel.MapViewModel
import com.siam.sky.ui.theme.HourCardSelected
import com.siam.sky.ui.theme.NavSurfaceBottom
import com.siam.sky.ui.theme.NavSurfaceTop
import com.siam.sky.ui.theme.NavStroke
import com.siam.sky.ui.theme.WeekCardStart

@Composable
fun MapView(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val viewModel: MapViewModel = viewModel(
        factory = MapViewModel.factory(UserRepo(UserLocalDataSource(context)))
    )

    val pickedLocation by viewModel.pickedLocation.collectAsState()
    val isLoading by viewModel.isLoadingCurrentLocation.collectAsState()
    val snapToLocation by viewModel.snapToLocation.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(pickedLocation.first, pickedLocation.second),
            12f
        )
    }

    // Animate camera when GPS snap triggers
    LaunchedEffect(snapToLocation) {
        snapToLocation?.let { (lat, lon) ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), 14f),
                durationMs = 800
            )
        }
    }

    val markerState = MarkerState(position = LatLng(pickedLocation.first, pickedLocation.second))

    // Keep marker in sync with picked location
    LaunchedEffect(pickedLocation) {
        markerState.position = LatLng(pickedLocation.first, pickedLocation.second)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // ── Full-screen map ──────────────────────────────────────────────────
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
                onInfoWindowClick = {},
                onClick = { false }
            )
        }

        // ── Top AppBar overlay ───────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(NavSurfaceTop, NavSurfaceBottom)
                    )
                )
                .border(
                    width = 1.dp,
                    color = NavStroke.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.map_screen_title),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // ── Bottom action panel ──────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(NavSurfaceTop, NavSurfaceBottom)
                    )
                )
                .border(
                    width = 1.dp,
                    color = NavStroke.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // "Use Current Location" button
            OutlinedButton(
                onClick = { viewModel.useCurrentLocation() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.dp, Color.White.copy(alpha = 0.35f)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.White.copy(alpha = 0.08f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = stringResource(R.string.btn_use_current_location),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // "Select This Location" button
            Button(
                onClick = { viewModel.confirmSelection(onNavigateBack) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(WeekCardStart, HourCardSelected)),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.btn_select_location),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
