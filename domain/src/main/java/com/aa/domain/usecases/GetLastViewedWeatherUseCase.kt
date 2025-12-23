package com.aa.domain.usecases

import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastViewedWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(): Flow<WeatherInfo?> {
        return repository.getLastViewedWeather()
    }
}