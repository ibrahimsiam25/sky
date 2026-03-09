package com.siam.sky.presentaion.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siam.sky.R
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.data.models.WeatherResponse
import com.siam.sky.ui.theme.WhiteFaded
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun WeatherHeroContent(weather: WeatherResponse, unit: AppUnit) {
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0] ?: Locale.getDefault()
    val now = remember { Calendar.getInstance() }
    val dateStr = remember(locale) { SimpleDateFormat("EEEE, MMM dd", locale).format(now.time) }
    val timeStr = remember(locale) { SimpleDateFormat("hh:mm a", locale).format(now.time) }
    val iconCode = weather.weather.firstOrNull()?.icon ?: "01d"
    val tempUnitStr = stringResource(
        when (unit) {
            AppUnit.METRIC   -> R.string.unit_temp_c
            AppUnit.IMPERIAL -> R.string.unit_temp_f
            AppUnit.STANDARD -> R.string.unit_temp_k
        }
    )

    val tempAnnotated = buildAnnotatedString {
        withStyle(SpanStyle(fontSize = 80.sp, fontWeight = FontWeight.Bold, color = Color.White)) {
            append("${weather.main.temp.toInt()}")
        }
        withStyle(SpanStyle(fontSize = 32.sp, fontWeight = FontWeight.Light, color = Color.White.copy(alpha = 0.85f), baselineShift = BaselineShift(0.25f))) {
            append(tempUnitStr.trim())
        }
    }

    val highLowAnnotated = buildAnnotatedString {
        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)) {
            append("H  ")
        }
        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)) {
            append("${weather.main.temp_max.toInt()}${tempUnitStr.trim()}")
        }
        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.4f), fontSize = 14.sp)) {
            append("   /   ")
        }
        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)) {
            append("L  ")
        }
        withStyle(SpanStyle(color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)) {
            append("${weather.main.temp_min.toInt()}${tempUnitStr.trim()}")
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        weather.name,
        modifier = Modifier.padding(horizontal = 24.dp),
        fontSize = 36.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        textAlign = TextAlign.Center,
        lineHeight = 46.sp
    )
    Text(
        text = tempAnnotated,
        modifier = Modifier.padding(horizontal = 24.dp),
        textAlign = TextAlign.Center,
        lineHeight = 88.sp
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
        fontSize = 15.sp, color = WhiteFaded, textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text("$dateStr  •  $timeStr", fontSize = 13.sp, color = WhiteFaded, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 24.dp))
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = highLowAnnotated,
        modifier = Modifier.padding(horizontal = 24.dp),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
    Image(
        painter = painterResource(id = R.drawable.house),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}
