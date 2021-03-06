package com.example.anch_kotiln.Controller

import com.example.anch_kotiln.Model.DTO.ModelDTO
import com.example.anch_kotiln.Model.DTO.ObjectDTO
import com.example.anch_kotiln.Model.DTO.VillagerDTO
import com.example.anch_kotiln.Model.VO.VersionVO
import com.example.anch_kotiln.Utility.IO
import com.google.gson.JsonElement
import retrofit2.Response

interface Controller {
    fun setCallback(_callback: VersionController.VersionCallback)
    fun request()
    fun jsonToData(): ArrayList<IO.Image>
}