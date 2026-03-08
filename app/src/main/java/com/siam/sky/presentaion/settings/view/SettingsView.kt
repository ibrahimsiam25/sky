package com.siam.sky.presentaion.settings.view

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLocaleManager
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.presentaion.settings.viewmodel.SettingsViewModel
import com.siam.sky.core.common.Background

@Composable
fun SettingsView() {
    val context = LocalContext.current
    val activity = context.findActivity()
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.factory(UserRepo(UserLocalDataSource(context)))
    )
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()

    SettingsScreenContent(
        selectedLanguage = selectedLanguage,
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
}

@Composable
private fun SettingsScreenContent(
    selectedLanguage: AppLanguage,
    onEnglishSelected: () -> Unit,
    onArabicSelected: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Background()
        SettingsContent(
            selectedLanguage = selectedLanguage,
            onEnglishSelected = onEnglishSelected,
            onArabicSelected = onArabicSelected
        )
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
