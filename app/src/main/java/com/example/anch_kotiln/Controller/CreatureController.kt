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

class CreatureController() : Controller {
    private val TAG = CreatureController::class.java.simpleName
    private val service = Network.instance.create(CreatureService::class.java)
    private val fishKey = "fish_value"
    private val insectKey = "insect_value"
    private val items = ArrayList<CreatureDTO>()
    private val images = ArrayList<IO.Image>()
    private var fishFinished = false
    private var insectFinished = false
    lateinit var versionCallback: VersionController.VersionCallback
    override fun setCallback(_callback: VersionController.VersionCallback) {
        versionCallback = _callback
    }

    override fun request() {}

    override fun jsonToData() {}

    fun requestFish() {
        service.getFishes().enqueue(object : Callback<List<FishVO>> {
            override fun onFailure(call: Call<List<FishVO>>, t: Throwable) {
                Log.d(TAG, "Request Fail!")
                Log.d(TAG, "message : ${t.message}")
            }

            override fun onResponse(call: Call<List<FishVO>>, response: Response<List<FishVO>>) {
                Log.d(TAG, "Request Success!")
                IO.preferenceManager.setValue("${IO.key.fish}${IO.key.value}", Network.gson.toJson(response.body()))
                jsonToFish()
                versionCallback.successRequest(images)
            }
        })
    }

    fun requestInsect() {
        service.getInsects().enqueue(object : Callback<List<InsectVO>> {
            override fun onFailure(call: Call<List<InsectVO>>, t: Throwable) {
                Log.d(TAG, "Request Fail!")
                Log.d(TAG, "message : ${t.message}")
            }

            override fun onResponse(
                call: Call<List<InsectVO>>,
                response: Response<List<InsectVO>>
            ) {
                Log.d(TAG, "Request Success!")
                IO.preferenceManager.setValue("${IO.key.insect}${IO.key.value}", Network.gson.toJson(response.body()))
                jsonToInsect()
                versionCallback.successRequest(images)
            }
        })
    }

    fun jsonToFish() {
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.key.fish}${IO.key.value}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, FishVO::class.java)
                items.add(
                    FishDTO(
                        "image/fish/icon-${element.engName}.png",
                        "image/fish/critterpedia-${element.engName}.png",
                        "image/fish/furniture-${element.engName}.png",
                        element.korName,
                        CreatureDTO.Where.valueOf(element.whereHow),
                        CreatureDTO.Weather.valueOf(element.weather),
                        element.date,
                        element.time,
                        element.sellPrice.toInt(),
                        FishDTO.Shadow.valueOf(element.shadow),
                        element.existFin,
                        "",
                        element.size
                    )
                )
                images.add(IO.Image("image/fish", "icon-${element.engName}.png"))
                images.add(IO.Image("image/fish", "critterpedia-${element.engName}.png"))
                images.add(IO.Image("image/fish", "furniture-${element.engName}.png"))
            }
        }
    }

    fun jsonToInsect() {
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.key.insect}${IO.key.value}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, InsectVO::class.java)
                items.add(
                    InsectDTO(
                        "image/insect/icon-${element.engName}.png",
                        "image/insect/critterpedia-${element.engName}.png",
                        "image/insect/furniture-${element.engName}.png",
                        element.korName,
                        CreatureDTO.Where.valueOf(element.whereHow),
                        CreatureDTO.Weather.valueOf(element.weather),
                        element.date,
                        element.time,
                        element.sellPrice.toInt(),
                        "",
                        element.size
                    )
                )
                images.add(IO.Image("image/insect", "icon-${element.engName}.png"))
                images.add(IO.Image("image/insect", "critterpedia-${element.engName}.png"))
                images.add(IO.Image("image/insect", "furniture-${element.engName}.png"))
            }
        }
    }

    override fun getImageNames(): ArrayList<IO.Image> {
        return images
    }

    override fun isFinished(): Boolean {
        return fishFinished.and(insectFinished)
    }


    fun getInsectItems(): ArrayList<InsectDTO> {
        val insectItems = ArrayList<InsectDTO>()
        items.forEach {
            if (it.type == ObjectDTO.Type.INSECT) {
                insectItems.add(it as InsectDTO)
            }
        }
        return insectItems
    }

    fun getFishItems(): ArrayList<FishDTO> {
        val fishItems = ArrayList<FishDTO>()
        items.forEach {
            if (it.type == ObjectDTO.Type.FISH) {
                fishItems.add(it as FishDTO)
            }
        }
        return fishItems
    }

    fun getRealtimeItems(): ArrayList<CreatureDTO> {
        val realtimeItems = ArrayList<CreatureDTO>()

        getRealtimeInsectItem()
            .forEach {
                realtimeItems.add(it)
            }
        getRealtimefishItem()
            .forEach {
                realtimeItems.add(it)
            }

        return realtimeItems
    }

    fun getRealtimeInsectItem(): ArrayList<CreatureDTO> {
        val realtimeInsectItem = ArrayList<CreatureDTO>()

        items.forEach {
            if (it.type == ObjectDTO.Type.INSECT && (it.dataValue[Common.getCurrentDate() - 1] && it.timeValue[Common.getCurrentTime()])) {
                realtimeInsectItem.add(it)
            }
        }
        return realtimeInsectItem
    }

    fun getRealtimefishItem(): ArrayList<CreatureDTO> {
        val realtimeFishItem = ArrayList<CreatureDTO>()

        items.forEach {
            if (it.type == ObjectDTO.Type.FISH && (it.dataValue[Common.getCurrentDate() - 1] && it.timeValue[Common.getCurrentTime()])) {
                realtimeFishItem.add(it)
            }
        }
        return realtimeFishItem
    }

    fun getRealtimeModel(): ArrayList<ModelDTO> {
        val result: ArrayList<ModelDTO> = ArrayList(0)

        if (getRealtimeInsectItem().size > 0) {
            result.add(ModelDTO(getRealtimeInsectItem() as ArrayList<ObjectDTO>))
        }
        if (getRealtimefishItem().size > 0) {
            result.add(ModelDTO(getRealtimefishItem() as ArrayList<ObjectDTO>))
        }
        return result
    }


    fun getModel(_type: ObjectDTO.Type): ArrayList<ModelDTO> {
        val result: ArrayList<ModelDTO> = ArrayList(0)
        when (_type) {
            ObjectDTO.Type.INSECT -> result.add(ModelDTO(getInsectItems() as ArrayList<ObjectDTO>))
            ObjectDTO.Type.FISH -> result.add(ModelDTO(getFishItems() as ArrayList<ObjectDTO>))
        }
        return result
    }
}