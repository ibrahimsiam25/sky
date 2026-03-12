package com.siam.sky.presentaion.alerts.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.siam.sky.R
import com.siam.sky.data.models.AlertType
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddAlertDialog(
    onDismiss: () -> Unit,
    onSave: (startTime: Long, endTime: Long, type: AlertType) -> Unit
) {
    val context = LocalContext.current
    var startTime by remember { mutableStateOf<Long?>(null) }
    var endTime by remember { mutableStateOf<Long?>(null) }
    var selectedType by remember { mutableStateOf(AlertType.NOTIFICATION) }
    var startError by remember { mutableStateOf<String?>(null) }
    var endError by remember { mutableStateOf<String?>(null) }

    val formatter = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }

    val startLabel = if (startTime != null)
        stringResource(R.string.start_time_label, formatter.format(Date(startTime!!)))
    else
        stringResource(R.string.select_start_time)

    val endButtonEnabled = startTime != null
    val endLabel = if (endTime != null)
        stringResource(R.string.end_time_label, formatter.format(Date(endTime!!)))
    else
        stringResource(R.string.select_end_time)

    val saveEnabled = startTime != null && endTime != null &&
            startError == null && endError == null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.add_new_alert),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                // --- Type selection ---
                Text(
                    text = stringResource(R.string.alert_type),
                    style = MaterialTheme.typography.labelLarge
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedType == AlertType.NOTIFICATION,
                        onClick = { selectedType = AlertType.NOTIFICATION }
                    )
                    Text(
                        text = stringResource(R.string.type_notification),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = selectedType == AlertType.ALARM,
                        onClick = { selectedType = AlertType.ALARM }
                    )
                    Text(
                        text = stringResource(R.string.type_alarm),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // --- Start time ---
                Button(
                    onClick = {
                        pickDateTime(context) { time ->
                            val now = System.currentTimeMillis()
                            if (time < now) {
                                startError = "past"
                                startTime = null
                            } else {
                                startError = null
                                startTime = time
                                // Re-validate end time
                                if (endTime != null && endTime!! <= time) {
                                    endError = "before_start"
                                } else {
                                    endError = null
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(text = startLabel, style = MaterialTheme.typography.labelLarge)
                }
                if (startError == "past") {
                    Text(
                        text = stringResource(R.string.error_past_time),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // --- End time ---
                Button(
                    onClick = {
                        pickDateTime(context) { time ->
                            val start = startTime ?: return@pickDateTime
                            if (time <= start) {
                                endError = "before_start"
                                endTime = null
                            } else {
                                endError = null
                                endTime = time
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = endButtonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (endButtonEnabled)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = if (!endButtonEnabled)
                            stringResource(R.string.select_end_time_hint)
                        else
                            endLabel,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (endButtonEnabled) Color.White else Color.White.copy(alpha = 0.4f)
                    )
                }
                if (endError == "before_start") {
                    Text(
                        text = stringResource(R.string.error_end_before_start),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (saveEnabled) {
                        onSave(startTime!!, endTime!!, selectedType)
                    }
                },
                enabled = saveEnabled
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
