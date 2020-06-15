package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.acnh.Model.FishVO
import com.example.anch_kotiln.Model.DTO.CreatureDTO
import com.example.anch_kotiln.Model.DTO.FishDTO
import com.example.anch_kotiln.Model.DTO.ModelDTO
import com.example.anch_kotiln.Model.DTO.ObjectDTO
import com.example.anch_kotiln.Service.CreatureService
import com.example.anch_kotiln.Utility.IO
import com.example.anch_kotiln.Utility.Network
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FishController : Controller {
    private val tag = CreatureController::class.java.simpleName
    private val service = Network.instance.create(CreatureService::class.java)
    private lateinit var items: ArrayList<FishDTO>
    private lateinit var versionCallback: VersionController.VersionCallback
    override fun setCallback(_callback: VersionController.VersionCallback) {
        versionCallback = _callback
    }

    override fun request() {
        service.getFishes().enqueue(object : Callback<List<FishVO>> {
            override fun onFailure(call: Call<List<FishVO>>, t: Throwable) {
                Log.d(tag, "Request Fail!")
                Log.d(tag, "message : ${t.message}")
                Thread.sleep(1000)
                request()
            }

            override fun onResponse(call: Call<List<FishVO>>, response: Response<List<FishVO>>) {
                Log.d(tag, "Request Success!")
                IO.preferenceManager.setValue("${IO.Key.FISH}${IO.Key.VALUE}", Network.gson.toJson(response.body()))
                versionCallback.successRequest(jsonToData())
            }
        })
    }

    override fun jsonToData(): ArrayList<IO.Image> {
        val result = ArrayList<FishDTO>()
        val images = ArrayList<IO.Image>()
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.Key.FISH}${IO.Key.VALUE}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, FishVO::class.java)
                result.add(
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
        items = result
        return images
    }

    fun getItems(): ArrayList<FishDTO> {
        return items
    }

    fun getModel(): ArrayList<ModelDTO> {
        val result: ArrayList<ModelDTO> = ArrayList(0)
        result.add(ModelDTO(items as ArrayList<ObjectDTO>))
        return result
    }
}