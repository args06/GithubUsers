package com.example.githubusers.data.remote.service

import com.example.githubusers.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserServiceAPI {

    private const val BASE_URL = "https://api.github.com"

    fun getApiService(): UserAPI {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders =
                req.newBuilder().addHeader("Authorization", BuildConfig.API_KEY).build()
            chain.proceed(requestHeaders)
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor).build()
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        return retrofit.create(UserAPI::class.java)
    }
}