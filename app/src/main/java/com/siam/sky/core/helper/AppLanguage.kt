package com.siam.sky.core.helper

enum class AppLanguage(val localeTag: String, val apiLanguage: String) {
    ENGLISH(localeTag = "en", apiLanguage = "en"),
    ARABIC(localeTag = "ar-EG", apiLanguage = "ar");

    companion object {
        fun fromLocaleTag(localeTag: String?): AppLanguage {
            return when (localeTag?.lowercase()) {
                "ar", "ar-eg" -> ARABIC
                else -> ENGLISH
            }
        }
    }
}