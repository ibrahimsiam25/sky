package com.siam.sky.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.siam.sky.R

private val InterFontFamily = FontFamily(
    Font(R.font.inter_18pt_light, FontWeight.Light),
    Font(R.font.inter_18pt_regular, FontWeight.Normal),
    Font(R.font.inter_18pt_medium, FontWeight.Medium),
    Font(R.font.inter_18pt_semibold, FontWeight.SemiBold),
    Font(R.font.inter_18pt_bold, FontWeight.Bold),
    Font(R.font.inter_18pt_extrabold, FontWeight.ExtraBold),
)

private val CairoFontFamily = FontFamily(
    Font(R.font.cairo_extralight, FontWeight.ExtraLight),
    Font(R.font.cairo_light, FontWeight.Light),
    Font(R.font.cairo_regular, FontWeight.Normal),
    Font(R.font.cairo_medium, FontWeight.Medium),
    Font(R.font.cairo_semibold, FontWeight.SemiBold),
    Font(R.font.cairo_bold, FontWeight.Bold),
    Font(R.font.cairo_extrabold, FontWeight.ExtraBold),
)

fun skyTypography(isArabic: Boolean): Typography {
    val fontFamily = if (isArabic) CairoFontFamily else InterFontFamily
    val bodyLetterSpacing = if (isArabic) 0.sp else 0.15.sp
    val labelLetterSpacing = if (isArabic) 0.sp else 0.2.sp

    fun style(
        fontWeight: FontWeight,
        fontSize: androidx.compose.ui.unit.TextUnit,
        lineHeight: androidx.compose.ui.unit.TextUnit,
        letterSpacing: androidx.compose.ui.unit.TextUnit = 0.sp,
    ) = TextStyle(
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontSize = fontSize,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    )

    return Typography(
        displayLarge = style(FontWeight.Bold, 57.sp, 64.sp),
        displayMedium = style(FontWeight.Bold, 45.sp, 52.sp),
        displaySmall = style(FontWeight.SemiBold, 36.sp, 44.sp),
        headlineLarge = style(FontWeight.Bold, 32.sp, 40.sp),
        headlineMedium = style(FontWeight.SemiBold, 28.sp, 36.sp),
        headlineSmall = style(FontWeight.SemiBold, 24.sp, 32.sp),
        titleLarge = style(FontWeight.SemiBold, 22.sp, 28.sp),
        titleMedium = style(FontWeight.SemiBold, 16.sp, 24.sp, bodyLetterSpacing),
        titleSmall = style(FontWeight.Medium, 14.sp, 20.sp, bodyLetterSpacing),
        bodyLarge = style(FontWeight.Normal, 16.sp, 24.sp, bodyLetterSpacing),
        bodyMedium = style(FontWeight.Medium, 14.sp, 20.sp, bodyLetterSpacing),
        bodySmall = style(FontWeight.Normal, 12.sp, 16.sp, bodyLetterSpacing),
        labelLarge = style(FontWeight.SemiBold, 14.sp, 20.sp, labelLetterSpacing),
        labelMedium = style(FontWeight.Medium, 12.sp, 16.sp, labelLetterSpacing),
        labelSmall = style(FontWeight.Medium, 11.sp, 16.sp, labelLetterSpacing),
    )
}