package com.example.retroffit_api_call_app.Services

import com.example.retroffit_api_call_app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            val client = OkHttpClient().newBuilder()

            // Logging for each request
            val logginInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            client.addInterceptor(logginInterceptor)

            retrofit = Retrofit.Builder()
                .client(client.build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!
    }
}