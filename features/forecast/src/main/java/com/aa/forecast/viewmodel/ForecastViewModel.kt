package com.aa.forecast.viewmodel

import androidx.lifecycle.viewModelScope
import com.aa.common.base.BaseViewModel
import com.aa.domain.usecases.GetForecastUseCase
import com.aa.domain.usecases.GetLastViewedWeatherUseCase
import com.aa.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getForecastUseCase: GetForecastUseCase,
    private val getLastWeatherUseCase: GetLastViewedWeatherUseCase
) : BaseViewModel<ForecastUiState>(ForecastUiState()) {

    fun onAction(action: ForecastIntentAction) {
        when (action) {
            is ForecastIntentAction.OnLoadForecast -> loadForecast(action.cityName)
        }
    }

    init {
        observeCurrentCity()
    }

    private fun observeCurrentCity() {
        getLastWeatherUseCase()
            .onEach { weatherInfo ->
                weatherInfo?.cityName?.let { city ->
                    onAction(ForecastIntentAction.OnLoadForecast(city))
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadForecast(city: String) {

        viewModelScope.launch {
            getForecastUseCase(city).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        updateState {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        updateState {
                            it.copy(
                                isLoading = false,
                                forecastList = result.data ?: emptyList(),
                                errorMessage = ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        updateState{
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message ?: "Unknown Error"
                            )
                        }
                    }
                }
            }
        }
    }
}