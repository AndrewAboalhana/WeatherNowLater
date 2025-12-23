package com.aa.common.database_entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    val cityName: String,

    val temperature: Double,
    val conditionText: String,
    val iconUrl: String,
    val humidity: Int,
    val windSpeed: Double,
    val lastUpdated: Long = System.currentTimeMillis()
)