package com.aa.domain.usecase

import app.cash.turbine.test
import com.aa.domain.util.Resource
import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import com.aa.domain.usecases.GetCurrentWeatherUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetCurrentWeatherUseCaseTest {

    private val repository: WeatherRepository = mockk()

    private val useCase = GetCurrentWeatherUseCase(repository)

    @Test
    fun `invoke calls repository and emits success`() = runTest {
        val city = "Cairo"
        val expectedWeather = WeatherInfo(
            cityName = "Cairo",
            temperature = 25.0,
            conditionText = "Sunny",
            iconUrl = "",
            humidity = 50,
            windSpeed = 10.0,
            date = ""
        )

        coEvery { repository.getCurrentWeather(city) } returns flowOf(Resource.Success(expectedWeather))

        useCase(city).test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(Resource.Success::class.java)
            assertThat(item.data?.cityName).isEqualTo("Cairo")
            awaitComplete()
        }
        coVerify(exactly = 1) { repository.getCurrentWeather("Cairo") }
    }

    @Test
    fun `invoke with empty city returns error`() = runTest {
        useCase("").test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(Resource.Error::class.java)
            assertThat(item.message).isEqualTo("City name cannot be empty")
            awaitComplete()
        }
    }

    @Test
    fun `invoke emits error when repository fails`() = runTest {
        val city = "UnknownCity"
        val errorMessage = "Network Error"

        coEvery { repository.getCurrentWeather(city) } returns flowOf(Resource.Error(errorMessage))

        useCase(city).test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(Resource.Error::class.java)
            assertThat(item.message).isEqualTo(errorMessage)
            awaitComplete()
        }
    }
}