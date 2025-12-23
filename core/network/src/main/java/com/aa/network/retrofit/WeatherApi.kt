package com.aa.network.retrofit

import com.aa.network.resources.ForecastResponseDto
import com.aa.network.resources.WeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
    ): WeatherDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
    ): ForecastResponseDto
}