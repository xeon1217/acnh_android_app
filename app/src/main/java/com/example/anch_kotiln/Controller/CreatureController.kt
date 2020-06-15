package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.acnh.Model.FishVO
import com.example.acnh.Model.InsectVO
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Model.DTO.*
import com.example.anch_kotiln.Model.VO.VersionVO
import com.example.anch_kotiln.Utility.Network
import com.example.anch_kotiln.Service.CreatureService
import com.example.anch_kotiln.Service.VersionService
import com.example.anch_kotiln.Utility.Common
import com.example.anch_kotiln.Utility.IO
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatureController {
    private val tag = CreatureController::class.java.simpleName
    val fishController = FishController()
    val insectController = InsectController()

    private fun getRealtimeInsectItem(): ArrayList<CreatureDTO> {
        val realtimeInsectItem = ArrayList<CreatureDTO>()

        insectController.getItems().forEach {
            if (it.type == ObjectDTO.Type.INSECT && (it.dataValue[Common.getCurrentDate() - 1] && it.timeValue[Common.getCurrentTime()])) {
                realtimeInsectItem.add(it)
            }
        }
        return realtimeInsectItem
    }

    private fun getRealtimefishItem(): ArrayList<CreatureDTO> {
        val realtimeFishItem = ArrayList<CreatureDTO>()

        fishController.getItems().forEach {
            if (it.type == ObjectDTO.Type.FISH && (it.dataValue[Common.getCurrentDate() - 1] && it.timeValue[Common.getCurrentTime()])) {
                realtimeFishItem.add(it)
            }
        }
        return realtimeFishItem
    }

    fun getModel(): ArrayList<ModelDTO> {
        val result: ArrayList<ModelDTO> = ArrayList(0)

        if (getRealtimeInsectItem().size > 0) {
            result.add(ModelDTO(getRealtimeInsectItem() as ArrayList<ObjectDTO>))
        }
        if (getRealtimefishItem().size > 0) {
            result.add(ModelDTO(getRealtimefishItem() as ArrayList<ObjectDTO>))
        }
        return result
    }
}