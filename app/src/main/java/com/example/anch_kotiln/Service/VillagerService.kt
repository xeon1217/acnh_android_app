package com.example.anch_kotiln.Service

import com.example.anch_kotiln.Model.VO.VillagerVO
import retrofit2.Call
import retrofit2.http.GET

interface VillagerService {
    @GET("/villager")
    fun getVillagers() : Call<List<VillagerVO>>
}