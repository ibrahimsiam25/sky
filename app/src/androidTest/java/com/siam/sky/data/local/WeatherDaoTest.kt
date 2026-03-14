package com.siam.sky.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.siam.sky.core.db.WeatherDataBase
import com.siam.sky.data.datasources.local.dao.WeatherDao
import com.siam.sky.data.models.*
import com.siam.sky.data.models.WeatherResponse
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WeatherDaoTest {
    lateinit var database: WeatherDataBase
    lateinit var dao: WeatherDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).build()
        dao = database.getWeatherDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @SmallTest
    @Test
    fun insertWeather_getById_returnsCorrectWeather() = runTest {
        val entity = createWeatherEntity()
        dao.insert(entity)
        val loaded = dao.getByKey("1")

        assertThat(loaded?.locationKey, `is`("1"))
        assertThat(loaded?.weatherResponse?.name, `is`("Cairo"))
    }
    @SmallTest
    @Test
    fun getByKey_noData_returnsNull() = runTest {

        val result = dao.getByKey("999")

        assertThat(result, `is`(nullValue()))
    }

    @SmallTest
    @Test
    fun insertWeather_replaceExisting_replacesOldWeather() = runTest {

        val first = createWeatherEntity()

        val second = createWeatherEntity().copy(
            weatherResponse = first.weatherResponse?.copy(
                name = "Cairo Updated"
            )
        )

        dao.insert(first)
        dao.insert(second)

        val loaded = dao.getByKey("1")

        assertThat(loaded?.weatherResponse?.name, `is`("Cairo Updated"))
    }
    private   fun createWeatherEntity(): WeatherEntity {
        return WeatherEntity(
            locationKey = "1",
            weatherResponse = WeatherResponse(
                coord = Coord(31.2357, 30.0492),
                weather = listOf(
                    WeatherItem(800, "Clear", "clear sky", "01d")
                ),
                base = "stations",
                main = MainWeather(
                    298.55, 298.74, 297.15, 300.15,
                    1013, 44, 1013, 1009
                ),
                visibility = 10000,
                wind = Wind(3.6, 350, null),
                clouds = Clouds(0),
                dt = 1710000000,
                sys = Sys("EG", 1709960000, 1710003000),
                timezone = 7200,
                id = 360630,
                name = "Cairo",
                cod = 200
            )
        )
    }
}

