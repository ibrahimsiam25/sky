package com.siam.sky.presentaion.favouirte.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.siam.sky.R
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.ui.theme.SettingsSectionBg

@Composable
fun FavouriteLocationCard(
    item: FavouriteLocationEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val regionName = item.name.ifBlank { stringResource(R.string.favourite_unknown_region) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SettingsSectionBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(36.dp)
                .padding(end = 4.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.favourite_region_label),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = regionName,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.delete),
                tint = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}