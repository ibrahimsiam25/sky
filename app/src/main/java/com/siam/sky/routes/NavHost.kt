package com.siam.sky.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.siam.sky.R
import com.siam.sky.presentaion.alerts.view.AlertsView
import com.siam.sky.presentaion.favouirte.view.FavouriteView
import com.siam.sky.presentaion.home.view.HomeView
import com.siam.sky.presentaion.map.view.MapView
import com.siam.sky.presentaion.settings.view.SettingsView
import com.siam.sky.ui.theme.NavSurfaceTop
import com.siam.sky.ui.theme.NavSurfaceBottom
import com.siam.sky.ui.theme.NavStroke
import com.siam.sky.ui.theme.NavLabelActive
import com.siam.sky.ui.theme.WeekCardStart
import com.siam.sky.ui.theme.HourCardSelected

// ─── Data class for nav items ────────────────────────────────────────────────

private data class BottomNavItem(
    val route: Route,
    val icon: ImageVector,
    val labelRes: Int,
)

private val navItems = listOf(
    BottomNavItem(Route.HomeView, Icons.Filled.Home, R.string.nav_home),
    BottomNavItem(Route.AlertsView, Icons.Filled.Notifications, R.string.nav_alerts),
    BottomNavItem(Route.FavouriteView, Icons.Filled.Favorite, R.string.nav_favourite),
    BottomNavItem(Route.SettingsView, Icons.Filled.Settings, R.string.nav_settings),
)

// ─── Routes that show the bottom nav bar ─────────────────────────────────────

private val topLevelRoutes: List<Route> = listOf(
    Route.HomeView,
    Route.AlertsView,
    Route.FavouriteView,
    Route.SettingsView,
)

// ─── App entry point ─────────────────────────────────────────────────────────

@Composable
fun App() {
    val controller = rememberNavController()
    val backStackEntry by controller.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val showBottomBar = topLevelRoutes.any { route ->
        currentDestination?.hasRoute(route::class) == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                SkyBottomBar(controller = controller)
            }
        },
        containerColor = Color.Transparent,
    ) { innerPadding ->
        NavHost(
            navController = controller,
            startDestination = Route.HomeView,
            modifier = Modifier.padding(bottom = if (showBottomBar) innerPadding.calculateBottomPadding() else 0.dp),
        ) {
            composable<Route.HomeView> { HomeView() }
            composable<Route.AlertsView> { AlertsView() }
            composable<Route.FavouriteView> { FavouriteView() }
            composable<Route.SettingsView> { SettingsView(onNavigateToMap = { controller.navigate(Route.MapView) }) }
            composable<Route.MapView> { MapView(onNavigateBack = { controller.popBackStack() }) }
        }
    }
}

// ─── Styled bottom bar ────────────────────────────────────────────────────────

@Composable
private fun SkyBottomBar(controller: NavController) {
    val backStackEntry by controller.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Column(modifier = Modifier.fillMaxWidth()) {
        // Decorative top separator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            NavStroke.copy(alpha = 0.35f),
                            NavStroke.copy(alpha = 0.35f),
                            Color.Transparent,
                        )
                    )
                )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NavSurfaceTop,
                            NavSurfaceBottom,
                        )
                    )
                )
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(top = 10.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navItems.forEach { item ->
                val isSelected = currentDestination?.hasRoute(item.route::class) == true
                SkyNavItem(
                    item = item,
                    isSelected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            controller.navigate(item.route) {
                                popUpTo(Route.HomeView) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                )
            }
        }
    }
}

// ─── Single nav item ──────────────────────────────────────────────────────────

@Composable
private fun SkyNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val activeIconColor = Color.White
    val activeLabelColor = NavLabelActive
    val inactiveColor = Color.White.copy(alpha = 0.40f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 4.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .then(
                    if (isSelected) {
                        Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        WeekCardStart.copy(alpha = 0.65f),
                                        HourCardSelected.copy(alpha = 0.45f),
                                    )
                                ),
                                RoundedCornerShape(22.dp),
                            )
                            .border(
                                width = 0.5.dp,
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.White.copy(alpha = 0.20f),
                                        Color.Transparent,
                                    )
                                ),
                                shape = RoundedCornerShape(22.dp),
                            )
                    } else Modifier
                )
                .padding(horizontal = 22.dp, vertical = 6.dp),
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(item.labelRes),
                tint = if (isSelected) activeIconColor else inactiveColor,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(Modifier.height(3.dp))
        Text(
            text = stringResource(item.labelRes),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) activeLabelColor else inactiveColor,
        )
    }
}
