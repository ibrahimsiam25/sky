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
import com.siam.sky.core.helper.AppUnit

@Composable
fun UnitsSection(
    selectedUint: AppUnit,
    onMetricSelected: () -> Unit,
    onStandardSelected: () -> Unit,
    onImperialSelected: () -> Unit
) {
    Column(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color(0x3D1F2448))
            .border(
                width = 1.dp,
                color = Color.Companion.White.copy(alpha = 0.14f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            )
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_unit_title),
            color = Color.Companion.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Companion.SemiBold
        )

        SettingOptionCard(
            title = stringResource(R.string.unit_metric),
            isSelected = selectedUint == AppUnit.METRIC,
            onClick = onMetricSelected
        )

        SettingOptionCard(
            title = stringResource(R.string.unit_standard),
            isSelected = selectedUint == AppUnit.STANDARD,
            onClick = onStandardSelected
        )

        SettingOptionCard(
            title = stringResource(R.string.unit_imperial),
            isSelected = selectedUint == AppUnit.IMPERIAL,
            onClick = onImperialSelected
        )


    }
}