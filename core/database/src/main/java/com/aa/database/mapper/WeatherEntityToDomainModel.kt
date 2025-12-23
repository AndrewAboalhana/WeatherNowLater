package com.aa.database.mapper

import com.aa.database.entity.WeatherEntity
import com.aa.domain.models.WeatherInfo

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

fun WeatherInfo.toEntityModel(): WeatherEntity{
    return WeatherEntity(
        cityName = cityName,
        temperature = temperature,
        conditionText = conditionText,
        iconUrl = iconUrl,
        humidity = humidity,
        windSpeed = windSpeed,
        lastUpdated = System.currentTimeMillis()
    )
}