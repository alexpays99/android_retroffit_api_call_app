package com.example.retroffit_api_call_app.models

import com.google.gson.annotations.SerializedName

data class Animal(
    val name: String,

    @SerializedName("latin_name")
    val latinName: String,

    @SerializedName("animal_type")
    val animalType: String,

    @SerializedName("active_time")
    val activeTime: String,

    @SerializedName("length_min")
    val lengthMin: String,

    @SerializedName("length_max")
    val lengthMax: String,

    @SerializedName("weight_min")
    val weightMin: String,

    @SerializedName("weight_max")
    val weightMax: String,

    val lifespan: String,
    val habitat: String,
    val diet: String,

    @SerializedName("geo_range")
    val geoRange: String,

    @SerializedName("image_link")
    val imageLink: String,

    val id: String
)
