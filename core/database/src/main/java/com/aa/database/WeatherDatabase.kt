package com.aa.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aa.database.dao.WeatherDao
import com.aa.database.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}