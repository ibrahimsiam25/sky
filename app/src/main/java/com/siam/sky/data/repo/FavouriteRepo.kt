package com.siam.sky.data.repo

import com.siam.sky.data.datasources.local.FavouriteLocalDataSource
import com.siam.sky.data.models.FavouriteLocationEntity
import kotlinx.coroutines.flow.Flow

class FavouriteRepo(
    private val favouriteLocalDataSource: FavouriteLocalDataSource
) {
    suspend fun insertFavourite(location: FavouriteLocationEntity) {
        favouriteLocalDataSource.insertFavourite(location)
    }

    fun observeAllFavourites(): Flow<List<FavouriteLocationEntity>> {
        return favouriteLocalDataSource.observeAllFavourites()
    }

    suspend fun deleteById(id: Int) {
        favouriteLocalDataSource.deleteById(id)
    }
}
