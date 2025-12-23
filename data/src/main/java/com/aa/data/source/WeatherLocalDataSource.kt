package com.aa.data.source

import com.aa.domain.models.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun getWeatherByCity(city: String): WeatherInfo?
    suspend fun insertWeather(weather: WeatherInfo)
    fun getLastViewedWeather(): Flow<WeatherInfo?>
    suspend fun clearAll()
}
