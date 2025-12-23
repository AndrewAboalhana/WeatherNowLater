package com.aa.data.repository

import app.cash.turbine.test
import com.aa.common.database_entity.WeatherEntity
import com.aa.common.dispatcher.DispatcherProvider
import com.aa.common.remote_resources.ForecastItemDto
import com.aa.common.remote_resources.ForecastResponseDto
import com.aa.common.remote_resources.MainDto
import com.aa.common.remote_resources.MainForecastDto
import com.aa.common.remote_resources.WeatherDescriptionDto
import com.aa.common.remote_resources.WeatherDto
import com.aa.data.source.WeatherLocalDataSource
import com.aa.data.source.WeatherRemoteDataSource
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
    private val localDataSource: WeatherLocalDataSource = mockk(relaxed = true)

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)

    private val dispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
        override val default: CoroutineDispatcher = testDispatcher
    }

    @Before
    fun setup() {
        repository = WeatherRepositoryImpl(remoteDataSource, localDataSource, dispatcherProvider)
    }

    // --- Current Weather Tests ---

    @Test
    fun `getCurrentWeather emits local data then remote data`() = runTest(testDispatcher) {
        val city = "Cairo"

        val localEntity = WeatherEntity("Cairo", 20.0, "Clear", "", 40, 5.0, 1000L)

        coEvery { localDataSource.getWeatherByCity(city) } returns localEntity

        val remoteDto = WeatherDto(
            name = "Cairo",
            main = MainDto(
                temp = 30.0,
                humidity = 50,
                feelsLike = 0.0,
                tempMin = 0.0,
                tempMax = 1000.0,
                pressure = 1000,
                seaLevel = 1000,
                grndLevel = 0
            ),
            weather = emptyList(),
            wind = null,
            coord = null,
            base = null,
            visibility = null,
            clouds = null,
            dt = null,
            sys = null,
            timezone = null,
            id = null,
            cod = null
        )

        coEvery { remoteDataSource.getCurrentWeather(city) } returns remoteDto

        repository.getCurrentWeather(city).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val localItem = awaitItem()
            assertThat(localItem).isInstanceOf(Resource.Success::class.java)
            assertThat(localItem.data?.temperature).isEqualTo(20.0)
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val finalItem = awaitItem()
            assertThat(finalItem).isInstanceOf(Resource.Success::class.java)
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            awaitComplete()
        }

        coVerify { localDataSource.insertWeather(any()) }
    }

    @Test
    fun `getCurrentWeather emits error when api fails and no cache`() = runTest(testDispatcher) {
        coEvery { localDataSource.getWeatherByCity(any()) } returns null

        coEvery { remoteDataSource.getCurrentWeather(any()) } throws IOException("Network Error")

        repository.getCurrentWeather("Cairo").test {
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
    fun `getForecast fetches from api and emits success`() = runTest(testDispatcher) {
        val city = "Dubai"
        val forecastDto = ForecastResponseDto(
            cod = "200", message = 0, cnt = 40, city = null,
            list = listOf(
                ForecastItemDto(
                    dt = 123456789,
                    dtTxt = "2025-12-22 12:00:00",
                    main = MainForecastDto(25.0, 24.0, 20.0, 30.0, 1010, 1010, 1010, 50, 0.0),
                    weather = listOf(WeatherDescriptionDto(800, "Clear", "Clear sky", "01d")),
                    clouds = null, wind = null, visibility = 10000, pop = 0.0, sys = null
                )
            )
        )

        coEvery { remoteDataSource.getForecast(city) } returns forecastDto

        repository.getForecast(city).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val successItem = awaitItem()
            assertThat(successItem).isInstanceOf(Resource.Success::class.java)

            val list = successItem.data
            assertThat(list).isNotEmpty()
            assertThat(list?.first()?.temperature).isEqualTo(25.0)

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

    @Test
    fun `getLastViewedWeather emits mapped domain model`() = runTest(testDispatcher) {
        val entity = WeatherEntity("London", 15.0, "Rain", "", 60, 10.0, 1000L)

        every { localDataSource.getLastViewedWeather() } returns flowOf(entity)

        repository.getLastViewedWeather().test {
            val item = awaitItem()
            assertThat(item).isNotNull()
            assertThat(item?.cityName).isEqualTo("London")
            assertThat(item?.temperature).isEqualTo(15.0)
            awaitComplete()
        }

        io.mockk.verify(exactly = 1) { localDataSource.getLastViewedWeather() }
    }
}