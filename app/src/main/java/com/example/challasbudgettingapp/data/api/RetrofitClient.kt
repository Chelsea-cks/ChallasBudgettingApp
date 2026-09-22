package com.example.challasbudgettingapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Base URL for the ASP.NET Core REST API.
    // The Android device connects to the development computer
    // through the computer's local network IP address.
    private const val BASE_URL =
        "http://192.168.10.157:5213/"
    // HTTP logging is enabled during development so that
    // API requests and responses can be monitored in Logcat.
    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val client =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    val apiService: ApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
}