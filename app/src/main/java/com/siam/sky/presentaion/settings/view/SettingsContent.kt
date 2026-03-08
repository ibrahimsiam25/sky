package com.siam.sky.presentaion.settings.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siam.sky.R
import com.siam.sky.core.helper.AppLanguage

@Composable
 fun SettingsContent(
    selectedLanguage: AppLanguage,
    onEnglishSelected: () -> Unit,
    onArabicSelected: () -> Unit
) {
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.Companion.height(12.dp))

        Text(
            text = stringResource(R.string.nav_settings),
            color = Color.Companion.White,
            fontSize = 34.sp,
            fontWeight = FontWeight.Companion.SemiBold
        )

        SettingsLanguageSection(
            selectedLanguage = selectedLanguage,
            onEnglishSelected = onEnglishSelected,
            onArabicSelected = onArabicSelected
        )
    }
}