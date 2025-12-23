package com.aa.domain.usecases

import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import com.aa.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Flow<Resource<List<WeatherInfo>>> {
        if (city.isBlank()) {
            return flowOf(Resource.Error("City name cannot be empty"))
        }
        return repository.getForecast(city)
    }
}