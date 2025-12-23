package com.aa.network


import com.aa.data.source.WeatherRemoteDataSource
import com.aa.domain.models.WeatherInfo
import com.aa.network.mapper.toDomainList
import com.aa.network.mapper.toDomainModel
import com.aa.network.retrofit.WeatherApi
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(city: String): WeatherInfo {
        return api.getCurrentWeather(city).toDomainModel()
    }

    override suspend fun getForecast(city: String): List<WeatherInfo> {
        return api.getForecast(city).toDomainList()
    }


}