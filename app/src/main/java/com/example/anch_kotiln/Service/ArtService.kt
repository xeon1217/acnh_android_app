package com.example.anch_kotiln.Service

import com.example.anch_kotiln.Model.VO.ArtVO
import retrofit2.Call
import retrofit2.http.GET

interface ArtService {
    @GET("/art")
    fun getArts(): Call<List<ArtVO>>
}