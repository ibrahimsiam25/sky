package com.siam.sky.presentaion.favouirte.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.siam.sky.core.ResponseState
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppUnit
import com.siam.sky.core.network.NetworkMonitor
import com.siam.sky.data.repo.UserRepo
import com.siam.sky.data.repo.WeatherRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class FavouriteWeatherViewModelTest {

    private lateinit var userRepo: UserRepo
    private lateinit var weatherRepo: WeatherRepo
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var favouriteWeatherViewModel: FavouriteWeatherViewModel

    private val testLat = 30.0444
    private val testLon = 31.2357

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        userRepo = mockk()
        weatherRepo = mockk()
        networkMonitor = mockk()


        every { userRepo.getSavedAppLanguage() } returns AppLanguage.ENGLISH
        every { userRepo.getSavedAppUnit() } returns AppUnit.METRIC
        every { userRepo.observeAppLanguage() } returns emptyFlow()
        every { userRepo.observeUnit() } returns emptyFlow()
        every { networkMonitor.observeConnectivity() } returns emptyFlow()
        every { weatherRepo.getCurrentWeather(any(), any(), any(), any()) } returns flowOf(ResponseState.Idle)

        favouriteWeatherViewModel = FavouriteWeatherViewModel(
            lat = testLat,
            lon = testLon,
            userRepo = userRepo,
            weatherRepo = weatherRepo,
            networkMonitor = networkMonitor
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun init_defaultUnitStateIsSetCorrectly() {
        val result = favouriteWeatherViewModel.unitState.value
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(AppUnit.METRIC))
    }

    @Test
    fun refresh_isRefreshingSetsToTrue() = runTest {
        val flowMock = flowOf(ResponseState.Loading)
        every { weatherRepo.getCurrentWeather(any(), any(), any(), any()) } returns flowMock

        favouriteWeatherViewModel.refresh()

        val result = favouriteWeatherViewModel.isRefreshing.value
        assertThat(result, `is`(true))
    }

    @Test
    fun fetchWeatherOnRefresh_updatesWeatherStateNotNull() = runTest {
        every { weatherRepo.getCurrentWeather(any(), any(), any(), any()) } returns flowOf(ResponseState.Loading)

        favouriteWeatherViewModel.refresh()

        val state = favouriteWeatherViewModel.weatherState.value
        assertThat(state, not(nullValue()))
        assertThat(state, `is`(ResponseState.Loading as ResponseState<*>))
    }
}
