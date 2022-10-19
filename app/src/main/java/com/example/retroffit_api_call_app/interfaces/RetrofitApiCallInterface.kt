package com.example.retroffit_api_call_app.interfaces

import com.example.retroffit_api_call_app.models.Animal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitApiCallInterface {
    @GET("{count}")
    fun getAnimals(@Path("count") count: String): Call<MutableList<Animal>>
}