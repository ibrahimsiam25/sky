package com.siam.sky.presentaion.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.siam.sky.R
import com.siam.sky.core.ApiState
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.core.helper.forecastDrawableForWeather
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.HourlyItem
import com.siam.sky.ui.theme.HourBorderReg
import com.siam.sky.ui.theme.HourBorderSel
import com.siam.sky.ui.theme.HourCardRegular
import com.siam.sky.ui.theme.HourCardSelected
import com.siam.sky.ui.theme.RainBlue
import com.siam.sky.ui.theme.WhiteFaded
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HourlySection(state: ApiState<HourlyForecastResponse>, unit: AppUnit) {
    when (state) {
        is ApiState.Loading -> Box(
            modifier = Modifier.fillMaxWidth().height(146.dp),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = Color.White, modifier = Modifier.size(28.dp)) }

        is ApiState.Success -> {
            val nowMs = System.currentTimeMillis()
            val items = state.data.list
            val nowIndex = items.indexOfFirst { it.dt * 1000L >= nowMs }.takeIf { it >= 0 } ?: 0

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(items) { index, item ->
                    HourlyCard(item = item, isNow = index == nowIndex, unit = unit)
                }
            }
        }

        is ApiState.Error -> Text(
            text = stringResource(R.string.hourly_unavailable),
            color = WhiteFaded,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        is ApiState.Idle -> Unit
    }
}

@Composable
fun HourlyCard(item: HourlyItem, isNow: Boolean, unit: AppUnit) {
    val configuration = LocalConfiguration.current
    val locale = configuration.locales[0] ?: Locale.getDefault()
    val timeLabel = if (isNow) stringResource(R.string.label_now)
    else SimpleDateFormat("h a", locale).format(Date(item.dt * 1000L))
    val tempUnitStr = stringResource(
        when (unit) {
            AppUnit.METRIC   -> R.string.unit_temp_c
            AppUnit.IMPERIAL -> R.string.unit_temp_f
            AppUnit.STANDARD -> R.string.unit_temp_k
        }
    )

    val iconRes = forecastDrawableForWeather(
        iconCode = item.weather.firstOrNull()?.icon ?: "01d",
    )
    val popPercent = (item.pop * 100).toInt()

    Box(
        modifier = Modifier
            .width(60.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(if (isNow) HourCardSelected else HourCardRegular)
            .border(
                width = 1.dp,
                color = if (isNow) HourBorderSel else HourBorderReg,
                shape = RoundedCornerShape(30.dp)
            )
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(30.dp), clip = false)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = timeLabel,
                maxLines = 1,
                overflow = TextOverflow.Visible,
                softWrap = false,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier.size(width = 44.dp, height = 38.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Fit
                )
                if (popPercent > 0) {
                    Text(
                        text = "$popPercent%",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.078).sp,
                        color = RainBlue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }

            Text(
                text = "${item.main.temp.toInt()}$tempUnitStr",
                maxLines = 1,
                overflow = TextOverflow.Visible,
                softWrap = false,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.5).sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}
