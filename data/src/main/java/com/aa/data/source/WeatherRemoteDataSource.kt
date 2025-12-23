package com.aa.data.source


import com.aa.domain.models.WeatherInfo

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(city: String): WeatherInfo
    suspend fun getForecast(city: String): List<WeatherInfo>
}