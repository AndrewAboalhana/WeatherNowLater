package com.aa.current_weather.viewmodel

import com.aa.common.base.BaseUiState
import com.aa.domain.models.WeatherInfo

data class CurrentWeatherUiState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false, // ممكن نستخدمها لو بنعمل Silent Refresh
    val error: String? = null
) : BaseUiState
