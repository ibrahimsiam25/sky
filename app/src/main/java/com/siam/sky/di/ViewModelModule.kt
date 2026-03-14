package com.siam.sky.di

import com.siam.sky.MainViewModel
import com.siam.sky.presentaion.alerts.viewmodel.AlertsViewModel
import com.siam.sky.presentaion.favouirte.viewmodel.FavouriteMapViewModel
import com.siam.sky.presentaion.favouirte.viewmodel.FavouriteViewModel
import com.siam.sky.presentaion.favouirte.viewmodel.FavouriteWeatherViewModel
import com.siam.sky.presentaion.home.viewmodel.HomeViewModel
import com.siam.sky.presentaion.settings.viewmodel.MapViewModel
import com.siam.sky.presentaion.settings.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single<MainViewModel> { MainViewModel(get()) }

    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { MapViewModel(get(), get()) }
    viewModel { AlertsViewModel(get()) }
    viewModel { FavouriteViewModel(get(), get()) }
    viewModel { FavouriteMapViewModel(get(), get()) }
    viewModel { (lat: Double, lon: Double) -> FavouriteWeatherViewModel(lat, lon, get(), get(), get()) }
}
