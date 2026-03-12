package com.siam.sky.core.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.siam.sky.data.datasources.local.AlertDao
import com.siam.sky.data.datasources.local.FavouriteLocationDao
import com.siam.sky.data.datasources.local.WeatherDao
import com.siam.sky.data.models.AlertModel
import com.siam.sky.data.models.FavouriteLocationEntity
import com.siam.sky.data.models.WeatherEntity

@Database(
    entities = [WeatherEntity::class, AlertModel::class, FavouriteLocationEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao
    abstract fun getAlertDao(): AlertDao
    abstract fun getFavouriteLocationDao(): FavouriteLocationDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDataBase? = null
        fun getInstance(ctx: Context): WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDataBase::class.java, "WeatherDataBase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
