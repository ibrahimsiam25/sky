package com.siam.sky.presentaion.alerts.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.siam.sky.R
import com.siam.sky.data.models.AlertModel
import com.siam.sky.data.models.AlertType
import com.siam.sky.ui.theme.SettingsSectionBg
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AlertItemCard(
    alert: AlertModel,
    onToggle: (AlertModel) -> Unit
) {
    val formatter = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }
    val startStr = formatter.format(Date(alert.startTime))
    val endStr = formatter.format(Date(alert.endTime))

    val typeLabel = when (alert.type) {
        AlertType.NOTIFICATION -> stringResource(R.string.type_notification)
        AlertType.ALARM -> stringResource(R.string.type_alarm)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SettingsSectionBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading icon
        Icon(
            imageVector = Icons.Filled.Notifications,
            contentDescription = null,
            tint = if (alert.isActive) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.3f),
            modifier = Modifier
                .size(36.dp)
                .padding(end = 4.dp)
        )

        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$typeLabel ${stringResource(R.string.nav_alerts)}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.start_time_label, startStr),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = stringResource(R.string.end_time_label, endStr),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        // Toggle switch
        Switch(
            checked = alert.isActive,
            onCheckedChange = { onToggle(alert) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = Color.White.copy(alpha = 0.6f),
                uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
    }
}
