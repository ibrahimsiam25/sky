package com.siam.sky.presentaion.home.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.siam.sky.R

@Composable
fun rememberShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )

    return Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = 0.08f),
            Color.White.copy(alpha = 0.22f),
            Color.White.copy(alpha = 0.08f),
        ),
        start = Offset(translate - 300f, translate - 300f),
        end = Offset(translate, translate),
    )
}

@Composable
fun HomeLoadingShimmer(modifier: Modifier = Modifier) {
    val shimmerBrush = rememberShimmerBrush()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 44.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ShimmerLine(
            brush = shimmerBrush,
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(42.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        ShimmerLine(
            brush = shimmerBrush,
            modifier = Modifier
                .fillMaxWidth(0.34f)
                .height(84.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        ShimmerLine(
            brush = shimmerBrush,
            modifier = Modifier
                .fillMaxWidth(0.30f)
                .height(18.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        ShimmerLine(
            brush = shimmerBrush,
            modifier = Modifier
                .fillMaxWidth(0.42f)
                .height(16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        ShimmerLine(
            brush = shimmerBrush,
            modifier = Modifier
                .fillMaxWidth(0.26f)
                .height(16.dp)
        )
        Spacer(modifier = Modifier.height(80.dp))

        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.house),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )

        }
    }
}

@Composable
private fun ShimmerLine(
    brush: Brush,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(brush)
    )
}

@Preview( showBackground = true , backgroundColor = 0xFF000000)
@Composable
fun HomeLoadingShimmerPreview() {
    HomeLoadingShimmer(modifier = Modifier.fillMaxWidth())
}