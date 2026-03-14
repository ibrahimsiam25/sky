package com.siam.sky

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.drawable.ColorDrawable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.siam.sky.core.helper.AppLocaleManager
import com.siam.sky.routes.App
import com.siam.sky.ui.theme.SkyTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by inject()

    override fun attachBaseContext(newBase: Context) {
        val localizedContext = AppLocaleManager.wrapContext(
            context = newBase,
            language = mainViewModel.getSavedAppLanguage()
        )
        super.attachBaseContext(localizedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        val savedLanguage = mainViewModel.getSavedAppLanguage()
        AppLocaleManager.applyLanguage(this, savedLanguage)
        super.onCreate(savedInstanceState)
        val systemBarColor = ContextCompat.getColor(this, R.color.system_bar_red)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(systemBarColor),
            navigationBarStyle = SystemBarStyle.dark(systemBarColor)
        )

        window.statusBarColor = systemBarColor
        window.navigationBarColor = systemBarColor
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false

        // Reduce edge-to-edge for nav bar only as fallback on OEMs that ignore transparent/color in gesture mode
        val contentView = findViewById<android.view.View>(android.R.id.content)
        window.setBackgroundDrawable(ColorDrawable(systemBarColor))

        setContent {
            SkyTheme {
                App()
            }
        }
    }
}
