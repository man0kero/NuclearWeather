package android.example.weatherapp.bussines.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherDataEntity::class, GeoCodeEntity::class], exportSchema = false, version = 2)
abstract class OpenWeatherDataBase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

    abstract fun getGeoCodeDao(): GeoCodeDao

}