package com.aa.forecast.viewmodel

import com.aa.common.base.BaseUiState
import com.aa.domain.models.WeatherInfo

data class ForecastUiState(
    val isLoading: Boolean = false,
    val forecastList: List<WeatherInfo> = emptyList(),
    val errorMessage: String = ""
) : BaseUiState
