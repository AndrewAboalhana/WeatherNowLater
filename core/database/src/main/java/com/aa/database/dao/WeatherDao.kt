package com.aa.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aa.common.database_entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("DELETE FROM weather_table")
    suspend fun clearAll()

    @Query("SELECT * FROM weather_table WHERE cityName = :cityName")
    suspend fun getWeatherByCity(cityName: String): WeatherEntity?

    @Query("SELECT * FROM weather_table ORDER BY lastUpdated DESC LIMIT 1")
    fun getLastViewedWeather(): Flow<WeatherEntity?>
}