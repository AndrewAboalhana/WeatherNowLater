package com.aa.domain.models

data class WeatherInfo(
    val cityName: String,
    val temperature: Double,
    val conditionText: String,
    val iconUrl: String,
    val humidity: Int,
    val windSpeed: Double,
    val date: String
)
