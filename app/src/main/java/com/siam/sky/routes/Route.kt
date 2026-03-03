package com.siam.sky.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {
    @Serializable
    object HomeView : Route()

}