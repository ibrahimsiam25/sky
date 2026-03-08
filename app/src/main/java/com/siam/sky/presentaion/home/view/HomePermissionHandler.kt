package com.siam.sky.presentaion.home.view

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.siam.sky.core.helper.LocationHelper
import com.siam.sky.presentaion.home.viewmodel.HomeViewModel
import com.siam.sky.presentaion.home.viewmodel.PermissionStatus

class HomePermissionHandler(
    private val viewModel: HomeViewModel,
) {
    @Composable
    fun Bind() {
        val context = LocalContext.current
        val activity = context.findActivity() ?: return
        val permissionStatus by viewModel.permissionStatus.collectAsState()

        val locationSettingsLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (LocationHelper.Companion.isLocationEnabled(activity)) {
                viewModel.setPermissionStatus(PermissionStatus.GRANTED)
            } else {
                viewModel.setPermissionStatus(PermissionStatus.UNKNOWN)
            }
        }

        val appSettingsLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            syncPermissionState(
                activity = activity,
                onNeedLocationSettings = {
                    locationSettingsLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            )
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            val granted = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    result[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                if (LocationHelper.Companion.isLocationEnabled(activity)) {
                    viewModel.setPermissionStatus(PermissionStatus.GRANTED)
                } else {
                    locationSettingsLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            } else {
                viewModel.setPermissionStatus(resolveDeniedStatus(activity))
            }
        }

        LaunchedEffect(Unit) {
            syncPermissionState(
                activity = activity,
                onNeedLocationSettings = {
                    locationSettingsLauncher.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                },
                onNeedPermissions = {
                    permissionLauncher.launch(locationPermissions())
                }
            )
        }

        LaunchedEffect(permissionStatus) {
            when (permissionStatus) {
                PermissionStatus.GRANTED -> Unit

                PermissionStatus.PERMANENTLY_DENIED -> {
                    appSettingsLauncher.launch(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    )
                }

                PermissionStatus.DENIED -> {
                    permissionLauncher.launch(locationPermissions())
                }

                PermissionStatus.UNKNOWN -> Unit
            }
        }
    }

    private fun locationPermissions(): Array<String> = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private fun resolveDeniedStatus(activity: ComponentActivity): PermissionStatus {
        val permanentlyDenied =
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) &&
                    !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)

        return if (permanentlyDenied) PermissionStatus.PERMANENTLY_DENIED else PermissionStatus.DENIED
    }

    private fun syncPermissionState(
        activity: ComponentActivity,
        onNeedLocationSettings: () -> Unit,
        onNeedPermissions: (() -> Unit)? = null,
    ) {
        when {
            !LocationHelper.Companion.checkPermission(activity) -> {
                viewModel.setPermissionStatus(PermissionStatus.UNKNOWN)
                onNeedPermissions?.invoke()
            }

            !LocationHelper.Companion.isLocationEnabled(activity) -> {
                viewModel.setPermissionStatus(PermissionStatus.UNKNOWN)
                onNeedLocationSettings()
            }

            else -> viewModel.setPermissionStatus(PermissionStatus.GRANTED)
        }
    }

    private tailrec fun Context.findActivity(): ComponentActivity? {
        return when (this) {
            is ComponentActivity -> this
            is ContextWrapper -> baseContext.findActivity()
            else -> null
        }
    }
}