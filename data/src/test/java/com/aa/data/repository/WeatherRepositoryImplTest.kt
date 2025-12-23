package com.aa.data.repository

import app.cash.turbine.test
import com.aa.common.dispatcher.DispatcherProvider
import com.aa.data.source.WeatherLocalDataSource
import com.aa.data.source.WeatherRemoteDataSource
import com.aa.domain.models.WeatherInfo
import com.aa.domain.util.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException


class WeatherRepositoryImplTest {

    private lateinit var repository: WeatherRepositoryImpl
    private val remoteDataSource: WeatherRemoteDataSource = mockk()

    private val localDataSource: WeatherLocalDataSource = mockk()

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private val dispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
    }

    @Before
    fun setup() {
        coEvery { localDataSource.insertWeather(any()) } returns Unit

        repository = WeatherRepositoryImpl(remoteDataSource, localDataSource, dispatcherProvider)
    }

    @Test
    fun `getCurrentWeather fetches from remote, saves to local, and emits success`() = runTest(testDispatcher) {
        val city = "Cairo"
        val mockDomainModel = WeatherInfo("Cairo", 30.0, "Clear", "01d", 50, 10.0, "2025")

        coEvery { localDataSource.getWeatherByCity(city) } returnsMany listOf(null, mockDomainModel)

        coEvery { remoteDataSource.getCurrentWeather(city) } returns mockDomainModel

        repository.getCurrentWeather(city).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val successItem = awaitItem()
            assertThat(successItem).isInstanceOf(Resource.Success::class.java)
            assertThat(successItem.data).isEqualTo(mockDomainModel)

            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            awaitComplete()
        }

        coVerify { localDataSource.insertWeather(mockDomainModel) }
        coVerify(exactly = 2) { localDataSource.getWeatherByCity(city) }
    }

    @Test
    fun `getCurrentWeather emits error when api fails`() = runTest(testDispatcher) {
        val city = "Cairo"

        coEvery { localDataSource.getWeatherByCity(any()) } returns null

        coEvery { remoteDataSource.getCurrentWeather(city) } throws IOException("Network Error")

        repository.getCurrentWeather(city).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val errorItem = awaitItem()
            assertThat(errorItem).isInstanceOf(Resource.Error::class.java)
            assertThat(errorItem.message).contains("Check internet connection")

            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            awaitComplete()
        }
    }

    // --- Forecast Tests ---

    @Test
    fun `getForecast fetches list from remote and emits success`() = runTest(testDispatcher) {
        val city = "Dubai"

        val mockForecastList = listOf(
            WeatherInfo("Dubai", 25.0, "Clear", "01d", 40, 5.0, "20-10-2025"),
            WeatherInfo("Dubai", 26.0, "Cloudy", "02d", 45, 6.0, "20-10-2025")
        )

        coEvery { remoteDataSource.getForecast(city) } returns mockForecastList

        repository.getForecast(city).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val successItem = awaitItem()
            assertThat(successItem).isInstanceOf(Resource.Success::class.java)
            assertThat(successItem.data).hasSize(2)
            assertThat(successItem.data?.first()?.temperature).isEqualTo(25.0)

            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            awaitComplete()
        }
    }

    @Test
    fun `getForecast emits error when api fails`() = runTest(testDispatcher) {
        val city = "Nowhere"
        coEvery { remoteDataSource.getForecast(city) } throws IOException("Server Down")

        repository.getForecast(city).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val errorItem = awaitItem()
            assertThat(errorItem).isInstanceOf(Resource.Error::class.java)
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            awaitComplete()
        }
    }

    // --- Last Viewed Weather Tests ---

    @Test
    fun `getLastViewedWeather emits domain model directly`() = runTest(testDispatcher) {
        val mockDomainModel = WeatherInfo("London", 15.0, "Rain", "10d", 60, 10.0, "20-10-2025")

        every { localDataSource.getLastViewedWeather() } returns flowOf(mockDomainModel)

        repository.getLastViewedWeather().test {
            val item = awaitItem()
            assertThat(item).isEqualTo(mockDomainModel)
            awaitComplete()
        }

        io.mockk.verify(exactly = 1) { localDataSource.getLastViewedWeather() }
    }
}