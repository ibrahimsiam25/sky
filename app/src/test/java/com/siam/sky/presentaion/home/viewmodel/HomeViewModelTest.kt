package com.siam.sky.presentaion.home.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.siam.sky.core.ResponseState
import com.siam.sky.core.helper.AppLanguage
import com.siam.sky.core.helper.AppLoctionMode
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
class HomeViewModelTest {

    private lateinit var userRepo: UserRepo
    private lateinit var weatherRepo: WeatherRepo
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        userRepo = mockk()
        weatherRepo = mockk()
        networkMonitor = mockk()


        every { userRepo.getSavedAppLanguage() } returns AppLanguage.ENGLISH
        every { userRepo.getSavedAppUnit() } returns AppUnit.METRIC
        every { userRepo.getSavedLocationMode() } returns AppLoctionMode.MAP
        every { userRepo.observeAppLanguage() } returns emptyFlow()
        every { userRepo.observeUnit() } returns emptyFlow()
        every { userRepo.observeLocationMode() } returns emptyFlow()
        every { userRepo.observeLastKnownLocation() } returns emptyFlow()
        every { networkMonitor.observeConnectivity() } returns emptyFlow()

        homeViewModel = HomeViewModel(userRepo, weatherRepo, networkMonitor)
    }



    @Test
    fun setPermissionStatus_updatesStateStatusIsNotNull() {
        // Trigger action
        homeViewModel.setPermissionStatus(PermissionStatus.GRANTED)

        // Assert
        val result = homeViewModel.permissionStatus.value
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(PermissionStatus.GRANTED))
    }

    @Test
    fun showPermissionDialog_updatesStateToTrue() {
        // Trigger action
        homeViewModel.showPermissionDialog()

        // Assert
        val result = homeViewModel.showPermissionDialog.value
        assertThat(result, `is`(true))
    }

    @Test
    fun fetchWeather_updatesWeatherState() = runTest {
        // Mock
        every { 
            weatherRepo.getCurrentWeather(any(), any(), any(), any()) 
        } returns flowOf(ResponseState.Loading)

        // Trigger action
        homeViewModel.fetchWeather(30.0, 31.0)

        // Assert state is not null and is Loading initially
        val result = homeViewModel.weatherState.value
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(ResponseState.Loading as ResponseState<*>))
    }
}
