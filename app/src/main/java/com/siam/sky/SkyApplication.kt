package com.siam.sky

import android.app.Application
import com.siam.sky.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SkyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SkyApplication)
            modules(appModules)
        }
    }
}
