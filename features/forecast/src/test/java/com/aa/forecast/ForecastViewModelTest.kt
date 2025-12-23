package com.aa.forecast

import app.cash.turbine.test
import com.aa.domain.models.WeatherInfo
import com.aa.domain.usecases.GetForecastUseCase
import com.aa.domain.usecases.GetLastViewedWeatherUseCase
import com.aa.domain.util.Resource
import com.aa.forecast.viewmodel.ForecastIntentAction
import com.aa.forecast.viewmodel.ForecastViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {

    private val getForecastUseCase: GetForecastUseCase = mockk()
    private val getLastWeatherUseCase: GetLastViewedWeatherUseCase = mockk()
    private lateinit var viewModel: ForecastViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `OnLoadForecast action updates state correctly`() = runTest {
        val city = "London"
        val mockForecastList = listOf(mockk<WeatherInfo>())

        every { getLastWeatherUseCase() } returns flowOf(null)

        coEvery { getForecastUseCase(city) } returns flow {
            emit(Resource.Loading(true))
            delay(10)
            emit(Resource.Success(mockForecastList))
        }

        viewModel = ForecastViewModel(getForecastUseCase, getLastWeatherUseCase)

        viewModel.state.test {
            awaitItem()

            viewModel.onAction(ForecastIntentAction.OnLoadForecast(city))

            assertThat(awaitItem().isLoading).isTrue()

            val successState = awaitItem()
            assertThat(successState.forecastList).isEqualTo(mockForecastList)
            assertThat(successState.isLoading).isFalse()
        }
    }
}