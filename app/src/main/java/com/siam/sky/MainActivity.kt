package com.siam.sky

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.graphics.drawable.ColorDrawable
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLocaleManager
import com.siam.sky.data.datasources.local.UserLocalDataSource
import com.siam.sky.routes.App
import com.siam.sky.ui.theme.SkyTheme

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val localizedContext = AppLocaleManager.wrapContext(
            context = newBase,
            language = UserLocalDataSource(newBase).getSavedAppLanguage()
        )
        super.attachBaseContext(localizedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val savedLanguage = UserLocalDataSource(this).getSavedAppLanguage()
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
        ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            view.setPadding(0, 0, 0, bottom)
            insets
        }

        setContent {
            SkyTheme {
                App()
            }
        }
    }
}
