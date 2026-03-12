package com.siam.sky.routes

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.siam.sky.presentaion.alerts.view.AlertsView
import com.siam.sky.presentaion.favouirte.view.FavouriteMapView
import com.siam.sky.presentaion.favouirte.view.FavouriteView
import com.siam.sky.presentaion.favouirte.view.FavouriteWeatherView
import com.siam.sky.presentaion.home.view.HomeView
import com.siam.sky.presentaion.settings.view.MapView
import com.siam.sky.presentaion.settings.view.SettingsView



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
                BottomBar(controller = controller)
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
            composable<Route.FavouriteView> {
                FavouriteView(
                    onNavigateToMap = { controller.navigate(Route.FavouriteMapView) },
                    onNavigateToDetails = { lat, lon ->
                        controller.navigate(Route.FavouriteWeatherView(lat, lon))
                    }
                )
            }
            composable<Route.SettingsView> { SettingsView(onNavigateToMap = { controller.navigate(Route.MapView) }) }
            composable<Route.MapView> { MapView(onNavigateBack = { controller.popBackStack() }) }
            composable<Route.FavouriteMapView> { FavouriteMapView(onNavigateBack = { controller.popBackStack() }) }
            composable<Route.FavouriteWeatherView> { backStackEntry ->
                val route = backStackEntry.toRoute<Route.FavouriteWeatherView>()
                FavouriteWeatherView(
                    lat = route.lat,
                    lon = route.lon,
                    onNavigateBack = { controller.popBackStack() }
                )
            }
        }
    }
}
