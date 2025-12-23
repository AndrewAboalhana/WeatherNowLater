package com.aa.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.aa.common.dispatcher.DispatcherProvider
import com.aa.data.source.WeatherLocalDataSource
import com.aa.data.source.WeatherRemoteDataSource
import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import com.aa.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    private val dispatchers: DispatcherProvider
) : WeatherRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getCurrentWeather(city: String): Flow<Resource<WeatherInfo>> = flow {
        emit(Resource.Loading(true))

        val localData = localDataSource.getWeatherByCity(city)
        if (localData != null) {
            emit(Resource.Success(localData))
            emit(Resource.Loading(true))
        }

        try {
            val remoteData = remoteDataSource.getCurrentWeather(city)

            localDataSource.insertWeather(remoteData)

            val updatedLocalData = localDataSource.getWeatherByCity(remoteData.cityName)

            if (updatedLocalData != null) {
                emit(Resource.Success(updatedLocalData))
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
        } catch (e: Throwable) {
            e.printStackTrace()
            emit(Resource.Error("Unexpected error: ${e.localizedMessage ?: "Unknown error"}"))
            emit(Resource.Loading(false))
        }
    }.flowOn(dispatchers.io)

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getForecast(city: String): Flow<Resource<List<WeatherInfo>>> = flow {
        emit(Resource.Loading(true))

        try {
            val response = remoteDataSource.getForecast(city)

            emit(Resource.Success(response))
            emit(Resource.Loading(false))

        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error("Couldn't load data. Check internet connection."))
            emit(Resource.Loading(false))
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error("Server Error: ${e.message}"))
            emit(Resource.Loading(false))
        } catch (e: Throwable) {
            e.printStackTrace()
            emit(Resource.Error("Unexpected error: ${e.localizedMessage ?: "Unknown error"}"))
            emit(Resource.Loading(false))
        }
    }.flowOn(dispatchers.io)

    override fun getLastViewedWeather(): Flow<WeatherInfo?> {
        return localDataSource.getLastViewedWeather().flowOn(dispatchers.io)
    }
}