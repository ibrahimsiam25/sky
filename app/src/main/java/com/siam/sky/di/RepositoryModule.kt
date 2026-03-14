package com.siam.sky.di

import com.siam.sky.data.repo.UserRepo
import com.siam.sky.data.repo.UserRepoImp
import com.siam.sky.data.repo.WeatherRepo
import com.siam.sky.data.repo.WeatherRepoImp
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepo> { UserRepoImp(get(), get()) }
    single<WeatherRepo> { WeatherRepoImp(get(), get(), get(), get()) }
}
