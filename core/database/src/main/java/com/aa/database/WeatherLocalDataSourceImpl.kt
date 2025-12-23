package com.aa.database

import com.aa.data.source.WeatherLocalDataSource
import com.aa.database.dao.WeatherDao
import com.aa.database.mapper.toDomainModel
import com.aa.database.mapper.toEntityModel
import com.aa.domain.models.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val dao: WeatherDao
) : WeatherLocalDataSource {

    override suspend fun getWeatherByCity(city: String): WeatherInfo? {
        return dao.getWeatherByCity(city)?.toDomainModel()
    }

    override suspend fun insertWeather(weather: WeatherInfo) {
        dao.insertWeather(weather.toEntityModel())
    }

    override fun getLastViewedWeather(): Flow<WeatherInfo?> {
        return dao.getLastViewedWeather().map { entity ->
            entity?.toDomainModel()
        }
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}