package com.siam.sky.data.datasources.local.imp

import com.siam.sky.data.datasources.local.FavouriteLocalDataSource
import com.siam.sky.data.datasources.local.dao.FavouriteLocationDao
import com.siam.sky.data.models.FavouriteLocationEntity
import kotlinx.coroutines.flow.Flow

class FavouriteLocalDataSourceImp(
    private val favouriteLocationDao: FavouriteLocationDao
) : FavouriteLocalDataSource {
    override suspend fun insertFavourite(location: FavouriteLocationEntity) {
        favouriteLocationDao.insertFavourite(location)
    }

    override fun observeAllFavourites(): Flow<List<FavouriteLocationEntity>> {
        return favouriteLocationDao.observeAllFavourites()
    }

    override suspend fun deleteById(id: Int) {
        favouriteLocationDao.deleteById(id)
    }
}
