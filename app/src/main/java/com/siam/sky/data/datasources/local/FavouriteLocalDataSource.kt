package com.siam.sky.data.datasources.local

import com.siam.sky.data.models.FavouriteLocationEntity
import kotlinx.coroutines.flow.Flow

class FavouriteLocalDataSource(
    private val favouriteLocationDao: FavouriteLocationDao
) {
    suspend fun insertFavourite(location: FavouriteLocationEntity) {
        favouriteLocationDao.insertFavourite(location)
    }

    fun observeAllFavourites(): Flow<List<FavouriteLocationEntity>> {
        return favouriteLocationDao.observeAllFavourites()
    }

    suspend fun deleteById(id: Int) {
        favouriteLocationDao.deleteById(id)
    }
}
