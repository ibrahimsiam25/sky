package com.siam.sky.data.datasources.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.siam.sky.data.models.FavouriteLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(location: FavouriteLocationEntity)

    @Query("SELECT * FROM favourite_locations ORDER BY addedAt DESC")
    fun observeAllFavourites(): Flow<List<FavouriteLocationEntity>>

    @Query("DELETE FROM favourite_locations WHERE id = :id")
    suspend fun deleteById(id: Int)
}
