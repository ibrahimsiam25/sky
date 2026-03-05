package com.siam.sky.data.repo

import android.location.Location
import com.siam.sky.data.datasources.local.UserLocalDataSource
import kotlinx.coroutines.flow.Flow

class UserRepo(private val userLocalDataSource: UserLocalDataSource) {

    fun getFreshLocation(): Flow<Location> {
        return userLocalDataSource.getFreshLocation()
    }
}
