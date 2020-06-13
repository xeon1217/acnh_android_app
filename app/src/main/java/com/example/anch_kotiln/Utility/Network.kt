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

    enum class Status(val status: String) {
        FIRST(""),
        FIRST_AND_FAIL_CONNECT_TO_SERVER(""),
        FAIL_CONNECT_TO_SERVER(""),
        REQUIRE_UPDATE(""),
        NOT_REQUIRE_UPDATE(""),
        SUCCESS_CONNECT_TO_SERVER("");

        override fun toString(): String {
            return super.toString()
        }
    }
}