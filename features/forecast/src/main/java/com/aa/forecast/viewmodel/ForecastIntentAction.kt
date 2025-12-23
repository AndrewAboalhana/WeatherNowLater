package com.aa.forecast.viewmodel

sealed interface ForecastIntentAction {
    data class OnLoadForecast(val cityName: String) : ForecastIntentAction
}