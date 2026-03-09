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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    Spacer(modifier = Modifier.height(20.dp))
    Text(weather.name,
        modifier = Modifier.padding(horizontal = 24.dp),
        fontSize = 38.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        textAlign = TextAlign.Center,
        lineHeight = 46.sp

    )
    Text(text =  "${weather.main.temp.toInt()}$tempUnitStr",
        modifier = Modifier.padding(horizontal = 24.dp),
        fontSize = 70.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center,
        lineHeight = 46.sp

    )

    Spacer(modifier = Modifier.height(2.dp))
    Text(
        text = weather.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercase() } ?: "",
        fontSize = 15.sp, color = WhiteFaded, textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text("$dateStr  •  $timeStr", fontSize = 13.sp, color = WhiteFaded, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 24.dp))
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = stringResource(R.string.high_low, weather.main.temp_max.toInt(), weather.main.temp_min.toInt(), tempUnitStr),
        fontSize = 15.sp,
        color = Color.White.copy(alpha = 0.8f),
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Image(
        painter = painterResource(id = R.drawable.house),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}
