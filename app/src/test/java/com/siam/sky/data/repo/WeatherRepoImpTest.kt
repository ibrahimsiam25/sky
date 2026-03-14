package com.siam.sky.data.repo



import androidx.test.ext.junit.runners.AndroidJUnit4
import com.siam.sky.core.ResponseState
import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.datasources.local.FavouriteLocalDataSource
import com.siam.sky.data.datasources.remote.WeatherRemoteDataSource
import com.siam.sky.data.models.Clouds
import com.siam.sky.data.models.Coord
import com.siam.sky.data.models.DailyCity
import com.siam.sky.data.models.DailyFeelsLike
import com.siam.sky.data.models.DailyForecastResponse
import com.siam.sky.data.models.DailyItem
import com.siam.sky.data.models.DailyTemp
import com.siam.sky.data.models.ForecastCity
import com.siam.sky.data.models.ForecastMain
import com.siam.sky.data.models.HourlyForecastResponse
import com.siam.sky.data.models.HourlyItem
import com.siam.sky.data.models.HourlySys
import com.siam.sky.data.models.MainWeather
import com.siam.sky.data.models.Sys
import com.siam.sky.data.models.WeatherItem
import com.siam.sky.data.models.WeatherResponse
import com.siam.sky.data.models.Wind
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WeatherRepoImpTest {
    private lateinit var repo: WeatherRepoImp
    private lateinit var fakeLocal: FakeWeatherDataSource

    private val remote: WeatherRemoteDataSource = mockk()
    private val favourite: FavouriteLocalDataSource = mockk(relaxed = true)
    private val networkMonitor: NetworkMonitor = mockk()

    @Before
    fun setup() {
        fakeLocal = FakeWeatherDataSource()

        repo = WeatherRepoImp(
            remote,
            fakeLocal,
            favourite,
            networkMonitor
        )
    }
    @Test
    fun getCurrentWeather_remoteSuccess_savesToCache() = runTest {

        val weather = createWeatherResponse()

        every { networkMonitor.isConnected() } returns true

        every {
            remote.getCurrentWeather(any(), any(), any(), any())
        } returns flowOf(ResponseState.Success(weather))

        val result = repo
            .getCurrentWeather(30.0, 31.0, "en", "metric")
            .first()

        val cached = fakeLocal.getWeather(30.0, 31.0)

        assertTrue(result is ResponseState.Success)
        assertEquals(weather, cached)
    }
    @Test
    fun getHourlyForecast_remoteError_returnsCache() = runTest {

        val hourly = createHourlyForecast()

        fakeLocal.saveHourlyForecast(30.0, 31.0, hourly)

        every { networkMonitor.isConnected() } returns true

        every {
            remote.getHourlyForecast(any(), any(), any())
        } returns flowOf(ResponseState.Error("error"))

        val result = repo
            .getHourlyForecast(30.0, 31.0, "cairo", "en", "metric")
            .first()

        assertEquals(hourly, (result as ResponseState.Success).data)
    }

    @Test
    fun getDailyForecast_noInternet_returnsCache() = runTest {

        val daily = createDailyForecast()

        fakeLocal.saveDailyForecast(30.0, 31.0, daily)

        every { networkMonitor.isConnected() } returns false

        val result = repo
            .getDailyForecast(30.0, 31.0, "cairo", "en", 7, "metric")
            .first()

        assertEquals(daily, (result as ResponseState.Success).data)
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

   private fun createDailyForecast(): DailyForecastResponse {
        return DailyForecastResponse(
            city = DailyCity(
                id = 360630,
                name = "Cairo",
                coord = Coord(
                    lon = 31.2357,
                    lat = 30.0444
                ),
                country = "EG",
                population = 20000000,
                timezone = 7200
            ),
            cod = "200",
            message = 0.0,
            cnt = 1,
            list = listOf(
                DailyItem(
                    dt = 1710000000,
                    sunrise = 1709960000,
                    sunset = 1710003000,
                    temp = DailyTemp(
                        day = 30.0,
                        min = 22.0,
                        max = 32.0,
                        night = 24.0,
                        eve = 28.0,
                        morn = 23.0
                    ),
                    feels_like = DailyFeelsLike(
                        day = 31.0,
                        night = 25.0,
                        eve = 29.0,
                        morn = 24.0
                    ),
                    pressure = 1013,
                    humidity = 40,
                    weather = listOf(
                        WeatherItem(
                            id = 800,
                            main = "Clear",
                            description = "clear sky",
                            icon = "01d"
                        )
                    ),
                    speed = 3.5,
                    deg = 250,
                    gust = null,
                    clouds = 0,
                    pop = 0.0
                )
            )
        )
    }
}