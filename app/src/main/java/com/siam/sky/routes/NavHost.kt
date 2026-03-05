package com.siam.sky.routes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.siam.sky.presentaion.home.view.HomeView

@Composable
fun App() {
    val controller = rememberNavController()
    NavHost(
        navController = controller,
        startDestination = Route.HomeView,
    ) {
        composable<Route.HomeView> {
            HomeView()
        }
    }
}
