package com.aa.network

import com.aa.common.remote_resources.ForecastResponseDto
import com.aa.common.remote_resources.WeatherDto
import com.aa.data.source.WeatherRemoteDataSource
import com.aa.network.retrofit.WeatherApi
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(city: String): WeatherDto {
        return api.getCurrentWeather(city)
    }

    override suspend fun getForecast(city: String): ForecastResponseDto {
        return api.getForecast(city)
    }
}