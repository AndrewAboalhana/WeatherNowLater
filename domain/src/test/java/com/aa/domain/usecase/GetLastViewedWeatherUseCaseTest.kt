package com.aa.domain.usecase

import app.cash.turbine.test
import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import com.aa.domain.usecases.GetLastViewedWeatherUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetLastViewedWeatherUseCaseTest {

    private val repository: WeatherRepository = mockk()
    private val useCase = GetLastViewedWeatherUseCase(repository)

    @Test
    fun `invoke calls repository and emits cached weather info`() = runTest {
        val expectedWeather = WeatherInfo(
            cityName = "London",
            temperature = 15.0,
            conditionText = "Cloudy",
            iconUrl = "//cdn.weatherapi.com/weather/64x64/day/116.png",
            humidity = 60,
            windSpeed = 12.0,
            date = "2023-10-05"
        )
        every { repository.getLastViewedWeather() } returns flowOf(expectedWeather)

        useCase().test {
            val item = awaitItem()

            assertThat(item).isNotNull()
            assertThat(item?.cityName).isEqualTo("London")
            assertThat(item?.temperature).isEqualTo(15.0)

            awaitComplete()
        }

        verify(exactly = 1) { repository.getLastViewedWeather() }
    }

    @Test
    fun `invoke returns null when cache is empty`() = runTest {
        every { repository.getLastViewedWeather() } returns flowOf(null)

        useCase().test {
            val item = awaitItem()
            assertThat(item).isNull()
            awaitComplete()
        }

        verify(exactly = 1) { repository.getLastViewedWeather() }
    }
}