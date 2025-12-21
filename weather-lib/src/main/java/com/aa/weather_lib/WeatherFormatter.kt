package com.aa.weather_lib

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object WeatherFormatter {

    fun formatForecastDate(dateString: String): String {
        return try {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val date = LocalDateTime.parse(dateString, inputFormatter)
            val outputFormatter = DateTimeFormatter.ofPattern("EEE, h a", Locale.ENGLISH)
            date.format(outputFormatter)
        } catch (e: Exception) {
            dateString
        }
    }

    fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("EEE, MMM d", Locale.ENGLISH)
        return LocalDateTime.now().format(formatter)
    }
}