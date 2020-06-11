package com.example.anch_kotiln.Service

import com.example.anch_kotiln.Model.VO.VersionVO
import retrofit2.Call
import retrofit2.http.GET

interface VersionService {
    @GET("/version")
    fun getAllVersions(): Call<List<VersionVO>>

    @GET("/version")
    fun get(): Call<List<String>>

    @GET("/version/villager")
    fun getVillagerVersion(): Call<VersionVO>

    @GET("/version/fish")
    fun getFishVersion(): Call<VersionVO>

    @GET("/version/insect")
    fun getInsectVersion(): Call<VersionVO>

    @GET("/version/reaction")
    fun getReactionVersion(): Call<VersionVO>

    @GET("/version/art")
    fun getArtVersion(): Call<VersionVO>
}