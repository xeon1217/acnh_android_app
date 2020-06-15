package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.acnh.Model.InsectVO
import com.example.anch_kotiln.Model.DTO.CreatureDTO
import com.example.anch_kotiln.Model.DTO.InsectDTO
import com.example.anch_kotiln.Model.DTO.ModelDTO
import com.example.anch_kotiln.Model.DTO.ObjectDTO
import com.example.anch_kotiln.Service.CreatureService
import com.example.anch_kotiln.Utility.IO
import com.example.anch_kotiln.Utility.Network
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsectController : Controller {
    private val tag = CreatureController::class.java.simpleName
    private val service = Network.instance.create(CreatureService::class.java)
    private lateinit var items: ArrayList<InsectDTO>
    private lateinit var versionCallback: VersionController.VersionCallback
    override fun setCallback(_callback: VersionController.VersionCallback) {
        versionCallback = _callback
    }

    override fun request() {
        service.getInsects().enqueue(object : Callback<List<InsectVO>> {
            override fun onFailure(call: Call<List<InsectVO>>, t: Throwable) {
                Log.d(tag, "Request Fail!")
                Log.d(tag, "message : ${t.message}")
                Thread.sleep(1000)
                request()
            }

            override fun onResponse(
                call: Call<List<InsectVO>>,
                response: Response<List<InsectVO>>
            ) {
                Log.d(tag, "Request Success!")
                IO.preferenceManager.setValue("${IO.Key.INSECT}${IO.Key.VALUE}", Network.gson.toJson(response.body()))
                versionCallback.successRequest(jsonToData())
            }
        })
    }

    override fun jsonToData(): ArrayList<IO.Image> {
        val result = ArrayList<InsectDTO>()
        val images = ArrayList<IO.Image>()
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.Key.INSECT}${IO.Key.VALUE}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, InsectVO::class.java)
                result.add(
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
        items = result
        return images
    }

    fun getItems(): ArrayList<InsectDTO> {
        return items
    }

    fun getModel(): ArrayList<ModelDTO> {
        val result: ArrayList<ModelDTO> = ArrayList(0)
        result.add(ModelDTO(items as ArrayList<ObjectDTO>))
        return result
    }
}