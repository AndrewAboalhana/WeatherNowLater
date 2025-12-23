package com.aa.data.source

import com.aa.common.remote_resources.ForecastResponseDto
import com.aa.common.remote_resources.WeatherDto

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(city: String): WeatherDto
    suspend fun getForecast(city: String): ForecastResponseDto
}