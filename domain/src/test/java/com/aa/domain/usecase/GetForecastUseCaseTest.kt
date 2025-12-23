package com.aa.domain.usecase

import app.cash.turbine.test
import com.aa.domain.util.Resource
import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import com.aa.domain.usecases.GetForecastUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetForecastUseCaseTest {

    private val repository: WeatherRepository = mockk()

    private val useCase = GetForecastUseCase(repository)

    @Test
    fun `invoke calls repository and emits success list`() = runTest {
        val city = "London"
        val expectedList = listOf(
            WeatherInfo("London", 15.0, "Rainy", "icon1", 80, 10.0, "Mon, 12 PM"),
            WeatherInfo("London", 18.0, "Cloudy", "icon2", 60, 12.0, "Tue, 12 PM")
        )

        coEvery { repository.getForecast(city) } returns flowOf(Resource.Success(expectedList))

        useCase(city).test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(Resource.Success::class.java)
            assertThat(item.data).isNotNull()
            assertThat(item.data).hasSize(2)
            assertThat(item.data?.first()?.conditionText).isEqualTo("Rainy")
            awaitComplete()
        }
        coVerify(exactly = 1) { repository.getForecast("London") }
    }

    @Test
    fun `invoke emits error when repository fails`() = runTest {
        val city = "UnknownCity"
        val errorMessage = "Network Error"
        coEvery { repository.getForecast(city) } returns flowOf(Resource.Error(errorMessage))
        useCase(city).test {
            val item = awaitItem()
            assertThat(item).isInstanceOf(Resource.Error::class.java)
            assertThat(item.message).isEqualTo(errorMessage)
            awaitComplete()
        }
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

}