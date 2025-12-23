package com.aa.database

import com.aa.common.database_entity.WeatherEntity
import com.aa.data.source.WeatherLocalDataSource
import com.aa.database.dao.WeatherDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val dao: WeatherDao
) : WeatherLocalDataSource {

    override suspend fun getWeatherByCity(city: String): WeatherEntity? {
        return dao.getWeatherByCity(city)
    }

    override suspend fun insertWeather(weather: WeatherEntity) {
        dao.insertWeather(weather)
    }

    override fun getLastViewedWeather(): Flow<WeatherEntity?> {
        return dao.getLastViewedWeather()
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }
}