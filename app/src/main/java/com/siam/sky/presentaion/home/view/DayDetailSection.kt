package com.siam.sky.presentaion.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.siam.sky.R
import com.siam.sky.core.helper.timestampToTime
import com.siam.sky.data.models.WeatherResponse
import com.siam.sky.ui.theme.CardBg
import com.siam.sky.ui.theme.WhiteFaded

@Composable
fun DayDetailSection(weather: WeatherResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(R.string.todays_details),
            fontSize = 14.sp,
            color = WhiteFaded,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.8.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DayDetailCard(Modifier.weight(1f), emoji = "💧", label = stringResource(R.string.label_humidity), value = "${weather.main.humidity}%")
            DayDetailCard(Modifier.weight(1f), emoji = "💨", label = stringResource(R.string.label_wind), value = "${weather.wind.speed} m/s")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DayDetailCard(Modifier.weight(1f), emoji = "🌡", label = stringResource(R.string.label_pressure), value = "${weather.main.pressure} hPa")
            DayDetailCard(Modifier.weight(1f), emoji = "☁️", label = stringResource(R.string.label_clouds), value = "${weather.clouds.all}%")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DayDetailCard(Modifier.weight(1f), emoji = "🌅", label = stringResource(R.string.label_sunrise), value = timestampToTime(
                weather.sys.sunrise
            )
            )
            DayDetailCard(Modifier.weight(1f), emoji = "🌇", label = stringResource(R.string.label_sunset), value = timestampToTime(
                weather.sys.sunset
            )
            )
        }
    }
}

@Composable
fun DayDetailCard(modifier: Modifier, emoji: String, label: String, value: String) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .padding(vertical = 16.dp, horizontal = 14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(text = emoji, fontSize = 20.sp)
            Text(text = label, fontSize = 11.sp, color = WhiteFaded, letterSpacing = 0.7.sp)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}
