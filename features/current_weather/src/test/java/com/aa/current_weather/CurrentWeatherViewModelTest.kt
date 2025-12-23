package com.aa.current_weather

import app.cash.turbine.test
import com.aa.current_weather.viewmodel.CurrentWeatherViewModel
import com.aa.domain.models.WeatherInfo
import com.aa.domain.usecases.GetLastViewedWeatherUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrentWeatherViewModelTest {

    private val getLastWeatherUseCase: GetLastViewedWeatherUseCase = mockk()
    private lateinit var viewModel: CurrentWeatherViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init collects weather data from use case`() = runTest(testDispatcher) {
        val mockWeather = mockk<WeatherInfo>(relaxed = true)

        every { getLastWeatherUseCase() } returns flowOf(mockWeather)

        viewModel = CurrentWeatherViewModel(getLastWeatherUseCase)

        viewModel.state.test {
            val firstState = awaitItem()
            if (firstState.weatherInfo == null) {
                val secondState = awaitItem()
                assertThat(secondState.weatherInfo).isEqualTo(mockWeather)
            } else {
                assertThat(firstState.weatherInfo).isEqualTo(mockWeather)
            }
        }
    }
}