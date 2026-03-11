package com.siam.sky.presentaion.settings.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLocaleManager
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.presentaion.settings.viewmodel.SettingsViewModel
import com.siam.sky.core.common.Background
import com.siam.sky.core.helper.AppLoctionMode
import com.siam.sky.core.helper.AppUnit

@Composable
fun SettingsView(onNavigateToMap: () -> Unit) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.factory(UserRepo(UserLocalDataSource(context)))
    )
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedUnit by viewModel.selectedUnit.collectAsState()
    val selectedLocationMode by viewModel.selectedLocationMode.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Background()
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
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

            LanguageSection(
                selectedLanguage =  selectedLanguage,
                        onEnglishSelected = {
                    if (viewModel.selectLanguage(AppLanguage.ENGLISH)) {
                        AppLocaleManager.applyLanguage(context, AppLanguage.ENGLISH)
                        activity?.recreate()
                    }
                },
                onArabicSelected = {
                    if (viewModel.selectLanguage(AppLanguage.ARABIC)) {
                        AppLocaleManager.applyLanguage(context, AppLanguage.ARABIC)
                        activity?.recreate()
                    }
                }
            )
            LocationSection(
                selectedLocation = selectedLocationMode,
                onGpsSelected = { viewModel.selectLocationMode(AppLoctionMode.GPS) },
                onMapSelected = {
                    viewModel.selectLocationMode(AppLoctionMode.MAP)
                    onNavigateToMap()
                }
            )

                UnitsSection(
                    selectedUint = selectedUnit,
                    onMetricSelected = { viewModel.selectUnit(AppUnit.METRIC) },
                    onStandardSelected = { viewModel.selectUnit(AppUnit.STANDARD) },

                    onImperialSelected = { viewModel.selectUnit(AppUnit.IMPERIAL) }
                )

        }

    }
}



private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext?.let {
            it.findActivity()
        }
        else -> null
    }
}
