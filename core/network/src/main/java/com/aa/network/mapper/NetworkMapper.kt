package com.aa.network.mapper

import com.aa.domain.models.WeatherInfo
import com.aa.network.resources.ForecastResponseDto
import com.aa.network.resources.WeatherDto

fun ForecastResponseDto.toDomainList(): List<WeatherInfo> {
    val dtoList = this.list ?: return emptyList()
    return dtoList
        .filter {
            it.dtTxt?.contains("12:00:00") == true
        }
        .map { item ->
            val iconCode = item.weather?.firstOrNull()?.icon ?: "01d"
            val dateStr = item.dtTxt ?: ""
            WeatherInfo(
                cityName = this.city?.name ?: "",
                temperature = item.main?.temp ?: 0.0,
                conditionText = item.weather?.firstOrNull()?.main ?: "Unknown",
                iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png",
                humidity = item.main?.humidity ?: 0,
                windSpeed = item.wind?.speed ?: 0.0,
                date = dateStr
            )
        }
}

fun WeatherDto.toDomainModel(): WeatherInfo {
    val iconCode = weather?.firstOrNull()?.icon ?: "01d"
    return WeatherInfo(
        cityName = name ?: "Unknown",
        temperature = main?.temp ?: 0.0,
        conditionText = weather?.firstOrNull()?.main ?: "Unknown",
        iconUrl = "https://openweathermap.org/img/wn/${iconCode}@4x.png",
        humidity = main?.humidity ?: 0,
        windSpeed = wind?.speed ?: 0.0,
        date = ""
    )
}