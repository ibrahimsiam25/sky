package com.siam.sky.presentaion.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siam.sky.R
import com.siam.sky.core.helper.AppLoction
import com.siam.sky.ui.theme.SettingsSectionBg

@Composable
fun LocationSection(
    selectedLocation: AppLoction,
    onGpsSelected: () -> Unit,
    onMapSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(SettingsSectionBg)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.14f),
                shape = RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_location_title),
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        SettingOptionCard(
            title = stringResource(R.string.gps),
            isSelected = selectedLocation == AppLoction.GPS,
            onClick = onGpsSelected
        )
        SettingOptionCard(
            title = stringResource(R.string.map),
            isSelected = selectedLocation == AppLoction.MAP,
            onClick = onMapSelected
        )
    }
}