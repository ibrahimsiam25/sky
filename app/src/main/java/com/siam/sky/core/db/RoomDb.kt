package com.siam.sky.core.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.siam.sky.data.datasources.local.WeatherDao
import com.siam.sky.data.models.WeatherModel

@Database(entities = [WeatherModel::class], version = 1)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun getWeatherDao(): WeatherDao

    companion object {
        @Volatile
        private var INSTANCE:  WeatherDataBase? = null
        fun getInstance(ctx: Context):  WeatherDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,  WeatherDataBase::class.java, " WeatherDataBase"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
