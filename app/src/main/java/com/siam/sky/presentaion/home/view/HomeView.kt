package com.siam.sky.presentaion.home.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siam.sky.R
import com.siam.sky.core.helper.LocationHelper
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.repo.UserRepo
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
        ) {
            if (location != null) {
                Text(
                    text = "Lat: ${location!!.latitude}  |  Lon: ${location!!.longitude}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Text(
                    text = "Fetching location�",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Button(onClick = {}) {
                Text("Click Me")
            }
        }
    }
}
