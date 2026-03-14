package com.siam.sky.di

import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSourceImp
import com.siam.sky.data.datasources.remote.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.dsl.module

private const val BASE_URL = "https://pro.openweathermap.org/"
private const val NETWORK_LOG_TAG = "SkyApi"

val networkModule = module {
    single<NetworkMonitor> { NetworkMonitor(get()) }

    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor { message -> android.util.Log.d(NETWORK_LOG_TAG, message) }
            .apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<WeatherService> {
        get<Retrofit>().create(WeatherService::class.java)
    }

    factory<WeatherRemoteDataSource> { WeatherRemoteDataSourceImp(get()) }
}
