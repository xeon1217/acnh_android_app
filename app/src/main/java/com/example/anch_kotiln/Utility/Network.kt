package com.example.anch_kotiln.Utility

import android.app.Application
import com.example.anch_kotiln.Service.VersionService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Network {
    companion object RetrofitClient {
        private const val PORT = "8080"
        private const val ADDRESS = "http://192.168.1.2"
        const val BASE_URL = "${ADDRESS}:${PORT}"
        //const val IMAGE_URL = "${BASE_URL}/image"
        val gson = GsonBuilder().setLenient().create()
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .build()
        val instance: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}