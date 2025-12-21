package com.aa.domain.repositories

import com.aa.domain.models.WeatherInfo
import com.aa.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getCurrentWeather(city: String): Flow<Resource<WeatherInfo>>

    suspend fun getForecast(city: String): Flow<Resource<List<WeatherInfo>>>
}