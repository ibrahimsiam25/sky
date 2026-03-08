package com.siam.sky.core.helper

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object AppLocaleManager {
    fun wrapContext(context: Context, language: AppLanguage): Context {
        val locale = Locale.forLanguageTag(language.localeTag)
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    fun applyLanguage(context: Context, language: AppLanguage) {
        val locale = Locale.forLanguageTag(language.localeTag)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}