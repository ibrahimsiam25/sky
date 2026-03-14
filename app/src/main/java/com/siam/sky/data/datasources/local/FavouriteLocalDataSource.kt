package com.siam.sky.data.datasources.local

import com.siam.sky.data.models.FavouriteLocationEntity
import kotlinx.coroutines.flow.Flow

interface FavouriteLocalDataSource {
    suspend fun insertFavourite(location: FavouriteLocationEntity)
    fun observeAllFavourites(): Flow<List<FavouriteLocationEntity>>
    suspend fun deleteById(id: Int)
}
