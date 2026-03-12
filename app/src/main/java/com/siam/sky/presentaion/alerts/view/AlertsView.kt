package com.siam.sky.presentaion.alerts.view

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siam.sky.R
import com.siam.sky.core.common.Background
import com.siam.sky.core.db.WeatherDataBase
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.models.AlertModel
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.presentaion.alerts.viewmodel.AlertsViewModel
import java.util.*

@Composable
fun AlertsView() {
    val context = LocalContext.current
    val database = remember { WeatherDataBase.getInstance(context) }
    val viewModel: AlertsViewModel = viewModel(
        factory = AlertsViewModel.factory(
            UserRepo(UserLocalDataSource(context, database.getAlertDao()))
        )
    )

    val alerts by viewModel.alertsState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showPermissionRationale by remember { mutableStateOf(false) }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) showDialog = true else showPermissionRationale = true
    }

    fun requestPermissionsThenOpen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val pm = context.packageManager
            val hasPermission = pm.checkPermission(
                Manifest.permission.POST_NOTIFICATIONS,
                context.packageName
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (hasPermission) showDialog = true
            else permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            showDialog = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Background()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            AlertsHeader()
            Spacer(modifier = Modifier.height(20.dp))
            AlertsList(
                alerts = alerts,
                onToggle = { viewModel.toggleAlert(it) },
                modifier = Modifier.weight(1f)
            )
        }

        FloatingActionButton(
            onClick = { requestPermissionsThenOpen() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 20.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_alert))
        }

        if (showDialog) {
            AddAlertDialog(
                onDismiss = { showDialog = false },
                onSave = { startTime, endTime, type ->
                    viewModel.addAlert(
                        AlertModel(startTime = startTime, endTime = endTime, type = type)
                    )
                    showDialog = false
                }
            )
        }

        if (showPermissionRationale) {
            AlertPermissionRationaleDialog(
                onDismiss = { showPermissionRationale = false },
                onRetry = {
                    showPermissionRationale = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            )
        }
    }
}

@Composable
private fun AlertsHeader() {
    Text(
        text = stringResource(R.string.nav_alerts),
        color = Color.White,
        fontSize = 34.sp,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
private fun AlertsList(
    alerts: List<AlertModel>,
    onToggle: (AlertModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(alerts, key = { it.id }) { alert ->
            AlertItemCard(alert = alert, onToggle = onToggle)
        }
    }
}

@Composable
private fun AlertPermissionRationaleDialog(
    onDismiss: () -> Unit,
    onRetry: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.permission_notification_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = stringResource(R.string.permission_notification_required),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(onClick = onRetry) {
                Text(stringResource(R.string.permission_retry))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

fun pickDateTime(context: android.content.Context, onTimePicked: (Long) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    onTimePicked(calendar.timeInMillis)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

