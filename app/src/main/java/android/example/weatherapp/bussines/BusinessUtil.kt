package android.example.weatherapp.bussines

import android.example.weatherapp.bussines.model.GeoCodeModel
import android.example.weatherapp.bussines.room.GeoCodeEntity

fun GeoCodeModel.mapToEntity() = GeoCodeEntity(
    this.name,
    this.local_names,
    this.lat,
    this.lon,
    this.country,
    this.state ?: "",
    this.isFavorite
)

fun GeoCodeEntity.mapToModel() = GeoCodeModel(
    this.name,
    local_names,
    this.lat,
    this.lon,
    this.country,
    this.state,
    this.isFavorite
)