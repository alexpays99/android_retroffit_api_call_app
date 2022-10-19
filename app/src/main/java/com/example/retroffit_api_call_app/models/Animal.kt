package com.example.retroffit_api_call_app.models

import com.squareup.moshi.Json

data class Animal (
    val name: String,

    @Json(name = "latin_name")
    val latinName: String,

    @Json(name = "animal_type")
    val animalType: String,

    @Json(name = "active_time")
    val activeTime: String,

    @Json(name = "length_min")
    val lengthMin: String,

    @Json(name = "length_max")
    val lengthMax: String,

    @Json(name = "weight_min")
    val weightMin: String,

    @Json(name = "weight_max")
    val weightMax: String,

    val lifespan: String,
    val habitat: String,
    val diet: String,

    @Json(name = "geo_range")
    val geoRange: String,

    @Json(name = "image_link")
    val imageLink: String,

    val id: Long
)
