package com.example.retroffit_api_call_app.common

import com.example.retroffit_api_call_app.Services.RetrofitService
import com.example.retroffit_api_call_app.interfaces.RetrofitApiCallInterface

object Common {
    private val BASE_URL = "https://zoo-animal-api.herokuapp.com/animals/rand/"
    val retrofitService: RetrofitApiCallInterface
        get() = RetrofitService.getClient(BASE_URL).create(RetrofitApiCallInterface::class.java)
//    val okHttpService: OkkHttpApiCallInterface
//        get() = OkHttpService.fetch(BASE_URL).create(RetrofitApiCallInterface::class.java)
}