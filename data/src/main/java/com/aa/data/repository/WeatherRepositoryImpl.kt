package com.aa.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.aa.common.dispatcher.DispatcherProvider
import com.aa.data.mapper.toDomainList
import com.aa.data.mapper.toDomainModel
import com.aa.data.mapper.toEntity
import com.aa.database.dao.WeatherDao
import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import com.aa.domain.util.Resource
import com.aa.network.retrofit.WeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao,
    private val dispatchers: DispatcherProvider
) : WeatherRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getCurrentWeather(city: String): Flow<Resource<WeatherInfo>> = flow {

        emit(Resource.Loading(true))
        val localData = dao.getWeatherByCity(city)
        if (localData != null) {
            emit(Resource.Success(localData.toDomainModel()))
            emit(Resource.Loading(true))
        }
        try {
            val remoteData = api.getCurrentWeather(city = city)
            dao.insertWeather(remoteData.toEntity())
            val updatedLocalData = dao.getWeatherByCity(remoteData.name ?: city)
            if (updatedLocalData != null) {
                emit(Resource.Success(updatedLocalData.toDomainModel()))
            }
            emit(Resource.Loading(false))

        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error("Couldn't load data. Check internet connection."))
            emit(Resource.Loading(false))

        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error("Server error: ${e.message}"))
            emit(Resource.Loading(false))
        }
    }.flowOn(dispatchers.io)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getForecast(city: String): Flow<Resource<List<WeatherInfo>>> = flow {
        emit(Resource.Loading(true))

        try {
            val response = api.getForecast(
                city = city,
            )

            val domainList = response.toDomainList()

            emit(Resource.Success(domainList))
            emit(Resource.Loading(false))

        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error("Network Error: Check your connection"))
            emit(Resource.Loading(false))
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error("Server Error: ${e.message}"))
            emit(Resource.Loading(false))
        }
    }.flowOn(dispatchers.io)
}