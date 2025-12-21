package com.aa.domain.usecases

import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import com.aa.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Flow<Resource<WeatherInfo>> {
        if (city.isBlank()) {
            return kotlinx.coroutines.flow.flowOf(Resource.Error("City name cannot be empty"))
        }
        return repository.getCurrentWeather(city)
    }
}