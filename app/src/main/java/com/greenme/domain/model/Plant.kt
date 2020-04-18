package com.greenme.domain.model

data class Plant(
    val id: String,
    var name: String,
    var date: String,
    val location: String,
    var photo: String,
    var latitude: Float,
    var longitude: Float
)