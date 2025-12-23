package com.aa.current_weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.aa.common.base.BaseViewModel
import com.aa.domain.usecases.GetLastViewedWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    private val getLastWeatherUseCase: GetLastViewedWeatherUseCase
) : BaseViewModel<CurrentWeatherUiState>(CurrentWeatherUiState()) {

    init {
        getCurrentWeather()
    }

    private fun getCurrentWeather() {
        getLastWeatherUseCase()
            .onEach { weatherInfo ->
                updateState {
                    it.copy(
                        weatherInfo = weatherInfo,
                        isLoading = false,
                        error = null
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}