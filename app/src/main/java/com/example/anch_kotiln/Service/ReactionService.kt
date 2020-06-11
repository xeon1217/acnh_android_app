package com.example.anch_kotiln.Service

import com.example.acnh.Model.ReactionVO
import retrofit2.Call
import retrofit2.http.GET

interface ReactionService {
    @GET("/reaction")
    fun getReaction() : Call<List<ReactionVO>>
}