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
        private const val PORT = "8080" // 포트
        private const val ADDRESS = "http://192.168.1.2" // 서버 주소
        const val BASE_URL = "${ADDRESS}:${PORT}"
        //const val IMAGE_URL = "${BASE_URL}/image"
        val gson = GsonBuilder().setLenient().create()
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS) // 연결에 성공하는데 기다리는 시간
            .readTimeout(3, TimeUnit.SECONDS) // 읽기가 성공하는데 기다리는 시간
            .writeTimeout(3, TimeUnit.SECONDS) // 쓰기가 성공하는데 기다리는 시간
            .build()
        val instance: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 서버와의 연결에 관련
    enum class Status(private val type: Int) {
        FIRST(0), // 처음으로 앱을 실행했을 때
        FIRST_AND_FAIL_CONNECT_TO_SERVER(1), // 처음으로 앱을 실행했으나, 서버와 연결을 하지 못했을 때
        FAIL_CONNECT_TO_SERVER(2), // 서버와 연결에 실패했을 때
        SUCCESS_CONNECT_TO_SERVER(3), // 서버와의 연결에 성공했을 때
        REQUIRE_UPDATE(4), // 업데이트가 필요할 때
        NOT_REQUIRE_UPDATE(5), // 업데이트가 필요하지 않을 때
        SUCCESS_UPDATE(6), // 업데이트를 성공했을 때
        CANCELED_UPDATE(7), // 업데이트를 취소했을 때
        ERROR_CANCELED_UPDATE(8); // 오류로 인해 업데이트가 취소되었을 때
        fun toInt(): Int {
            return type
        }
    }
}