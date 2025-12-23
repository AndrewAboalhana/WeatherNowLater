package com.aa.data.mapper


import com.aa.common.database_entity.WeatherEntity
import com.aa.common.remote_resources.ForecastResponseDto
import com.aa.common.remote_resources.WeatherDto
import com.aa.domain.models.WeatherInfo

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

fun WeatherDto.toEntity(): WeatherEntity {
    val iconCode = weather?.firstOrNull()?.icon ?: "01d"
    return WeatherEntity(
        cityName = name ?: "Unknown",
        temperature = main?.temp ?: 0.0,
        conditionText = weather?.firstOrNull()?.main ?: "Unknown",
        iconUrl = "https://openweathermap.org/img/wn/${iconCode}@4x.png",
        humidity = main?.humidity ?: 0,
        windSpeed = wind?.speed ?: 0.0,
        lastUpdated = System.currentTimeMillis()
    )
}

fun WeatherEntity.toDomainModel(): WeatherInfo {
    return WeatherInfo(
        cityName = cityName,
        temperature = temperature,
        conditionText = conditionText,
        iconUrl = iconUrl,
        humidity = humidity,
        windSpeed = windSpeed,
        date = ""
    )
}

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