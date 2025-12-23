package com.aa.search.viewmodel

import androidx.lifecycle.viewModelScope
import com.aa.common.base.BaseViewModel
import com.aa.domain.usecases.GetCurrentWeatherUseCase
import com.aa.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getCurrentWeather: GetCurrentWeatherUseCase
) : BaseViewModel<SearchUiState>(SearchUiState()) {

    private var searchJob: Job? = null
    private val debounceDelay = 500L
    fun onQueryChange(newQuery: String) {
        updateState { it.copy(query = newQuery, error = null, isSearchSuccessful = false) }
        searchJob?.cancel()
        if (newQuery.isBlank()) {
            updateState { it.copy(isLoading = false) }
            return
        }
        searchJob = viewModelScope.launch {
            delay(debounceDelay)

            executeSearch(newQuery)
        }
    }

    private suspend fun executeSearch(query: String) {
        getCurrentWeather(query).collect { resource ->
            when (resource) {
                is Resource.Loading -> {
                    updateState { it.copy(isLoading = resource.isLoading) }
                }
                is Resource.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            isSearchSuccessful = true,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            error = resource.message ?: "Unknown Error"
                        )
                    }
                }
            }
        }
    }
}