package com.aa.domain.usecases

import com.aa.domain.models.WeatherInfo
import com.aa.domain.repositories.WeatherRepository
import com.aa.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Flow<Resource<List<WeatherInfo>>> {
        return repository.getForecast(city)
    }
}