package com.siam.sky.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.siam.sky.core.db.WeatherDataBase
import com.siam.sky.data.datasources.local.dao.WeatherDao
import com.siam.sky.data.datasources.local.imp.WeatherLocalDataSourceImp
import com.siam.sky.data.models.*
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class WeatherLocalDataSourceTest {
    lateinit var database: WeatherDataBase
    lateinit var dao: WeatherDao
    lateinit var localDataSource: WeatherLocalDataSourceImp

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getWeatherDao()
        localDataSource = WeatherLocalDataSourceImp(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun saveWeather_getWeather_returnsSuccess() = runTest {
        val lat = 30.0492
        val lon = 31.2357

        val weather = createWeatherResponse()

        localDataSource.saveCurrentWeather(lat, lon, weather)

        val result = localDataSource.getWeather(lat, lon)

        assertThat( result?.name, `is`("Cairo"))
    }

    @Test
    fun getWeather_noData_returnsNull() = runTest {

        val lat = 30.0492
        val lon = 31.2357

        val result = localDataSource.getWeather(lat, lon)

        assertThat(result, `is`(nullValue()))
    }

    @Test
    fun saveHourlyForecast_getHourly_returnsHourlyData() = runTest {

        val lat = 30.0492
        val lon = 31.2357

        val hourly = createHourlyForecast()

        localDataSource.saveHourlyForecast(lat, lon, hourly)

        val result = localDataSource.getHourly(lat, lon)

        assertThat(result, `is`(hourly))
    }

 private   fun createWeatherResponse(): WeatherResponse {
        return WeatherResponse(
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
    }

    private   fun createHourlyForecast(): HourlyForecastResponse {
        return HourlyForecastResponse(
            cod = "200",
            message = 0,
            cnt = 1,
            list = listOf(
                HourlyItem(
                    dt = 1710000000,
                    main = ForecastMain(
                        temp = 298.55,
                        feels_like = 298.74,
                        temp_min = 297.15,
                        temp_max = 300.15,
                        pressure = 1013,
                        sea_level = 1013,
                        grnd_level = 1009,
                        humidity = 44,
                        temp_kf = null
                    ),
                    weather = listOf(
                        WeatherItem(
                            id = 800,
                            main = "Clear",
                            description = "clear sky",
                            icon = "01d"
                        )
                    ),
                    clouds = Clouds(all = 0),
                    wind = Wind(
                        speed = 3.6,
                        deg = 350,
                        gust = null
                    ),
                    visibility = 10000,
                    pop = 0.0,
                    sys = HourlySys(pod = "d"),
                    dt_txt = "2024-03-10 12:00:00"
                )
            ),
            city = ForecastCity(
                id = 360630,
                name = "Cairo",
                coord = Coord(
                    lon = 31.2357,
                    lat = 30.0492
                ),
                country = "EG",
                population = 20000000,
                timezone = 7200,
                sunrise = 1709960000,
                sunset = 1710003000
            )
        )
    }
}
