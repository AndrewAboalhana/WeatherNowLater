package com.aa.search

import app.cash.turbine.test
import com.aa.domain.usecases.GetCurrentWeatherUseCase
import com.aa.domain.util.Resource
import com.aa.search.viewmodel.SearchViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase = mockk()
    private lateinit var viewModel: SearchViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(getCurrentWeatherUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onQueryChange triggers search after debounce delay`() = runTest(testDispatcher) {
        val city = "Dubai"

        coEvery { getCurrentWeatherUseCase(city) } returns flowOf(
            Resource.Loading(true),
            Resource.Success(mockk(relaxed = true)),
        )

        viewModel.state.test {
            awaitItem()
            viewModel.onQueryChange(city)

            val typingState = awaitItem()
            assertThat(typingState.query).isEqualTo(city)
            assertThat(typingState.isLoading).isFalse()

            testDispatcher.scheduler.advanceTimeBy(510)
            runCurrent()

            val resultState = awaitItem()
            if (resultState.isLoading) {
                val successState = awaitItem()
                assertThat(successState.isSearchSuccessful).isTrue()
            } else {
                assertThat(resultState.isSearchSuccessful).isTrue()
            }
            coVerify(exactly = 1) { getCurrentWeatherUseCase(city) }
        }
    }

    @Test
    fun `typing fast cancels previous search (Debounce Check)`() = runTest {

        coEvery { getCurrentWeatherUseCase("Cai") } returns flowOf(Resource.Success(mockk(relaxed = true)))

        viewModel.onQueryChange("C")
        testDispatcher.scheduler.advanceTimeBy(100)

        viewModel.onQueryChange("Ca")
        testDispatcher.scheduler.advanceTimeBy(100)

        viewModel.onQueryChange("Cai")
        testDispatcher.scheduler.advanceTimeBy(501)

        coVerify(exactly = 1) { getCurrentWeatherUseCase("Cai") }
        coVerify(exactly = 0) { getCurrentWeatherUseCase("C") }
        coVerify(exactly = 0) { getCurrentWeatherUseCase("Ca") }
    }
}