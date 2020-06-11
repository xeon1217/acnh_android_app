package com.example.anch_kotiln.Service

import com.example.acnh.Model.FishVO
import com.example.acnh.Model.InsectVO
import com.example.anch_kotiln.Model.VO.VillagerVO
import retrofit2.Call
import retrofit2.http.GET

interface CreatureService {
    @GET("/fish")
    fun getFishes() : Call<List<FishVO>>

    @GET("/insect")
    fun getInsects() : Call<List<InsectVO>>
}