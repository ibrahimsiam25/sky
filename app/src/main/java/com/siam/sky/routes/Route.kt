package com.siam.sky.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Route {

    @Serializable
    object HomeView : Route()

    @Serializable
    object AlertsView : Route()

    @Serializable
    object FavouriteView : Route()

    @Serializable
    object SettingsView : Route()

    @Serializable
    object MapView : Route()

    @Serializable
    object FavouriteMapView : Route()
}