package com.greenme.datasource.model

import com.greenme.domain.model.Plant
import com.squareup.moshi.Json

data class PlantEntity(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "date") val date: String?,
    @field:Json(name = "location") val location: String?,
    @field:Json(name = "photo") val photo: String?,
    @field:Json(name = "latitude") val latitude: Float?,
    @field:Json(name = "longitude") val longitude: Float?
)

fun PlantEntity.mapToDomain(): Plant =
    Plant(id, name?:"", date?:"", location?:"", photo?:"", latitude?:0f, longitude?:0f)

fun List<PlantEntity>.mapToDomain(): List<Plant> = map { it.mapToDomain() }

fun Plant.mapToData(): PlantEntity =
    PlantEntity(id, name?:"", date?:"", location?:"", photo?:"", latitude?:0f, longitude?:0f)
