package com.aa.weathernowlater.di

import com.aa.data.source.WeatherLocalDataSource
import com.aa.data.source.WeatherRemoteDataSource
import com.aa.database.WeatherLocalDataSourceImpl
import com.aa.network.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRemoteDataSource(
        impl: WeatherRemoteDataSourceImpl
    ): WeatherRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindWeatherLocalDataSource(
        impl: WeatherLocalDataSourceImpl
    ): WeatherLocalDataSource
}