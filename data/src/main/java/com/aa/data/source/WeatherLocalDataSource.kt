package com.aa.data.source

import com.aa.common.database_entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun getWeatherByCity(city: String): WeatherEntity?
    suspend fun insertWeather(weather: WeatherEntity)
    fun getLastViewedWeather(): Flow<WeatherEntity?>
    suspend fun clearAll()
}
