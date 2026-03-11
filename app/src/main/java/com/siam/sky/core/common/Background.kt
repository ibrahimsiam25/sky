package com.siam.sky.core.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.siam.sky.R
import com.siam.sky.ui.theme.BgOverlayTop
import com.siam.sky.ui.theme.BgOverlayMid
import com.siam.sky.ui.theme.BgOverlayBottom

@Composable
 fun Background() {
    Image(
        painter = painterResource(R.drawable.homebackground),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BgOverlayTop,
                        BgOverlayMid,
                        BgOverlayBottom
                    )
                )
            )
    )
}