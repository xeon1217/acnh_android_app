package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.acnh.Model.ReactionVO
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Model.DTO.ObjectDTO
import com.example.anch_kotiln.Model.DTO.ReactionDTO
import com.example.anch_kotiln.Model.DTO.VillagerDTO
import com.example.anch_kotiln.Model.VO.VersionVO
import com.example.anch_kotiln.Service.ReactionService
import com.example.anch_kotiln.Service.VersionService
import com.example.anch_kotiln.Utility.IO
import com.example.anch_kotiln.Utility.Network
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReactionController() : Controller {
    private val TAG = ReactionController::class.java.simpleName
    private val service = Network.instance.create(ReactionService::class.java)
    private lateinit var items: ArrayList<ReactionDTO>
    lateinit var versionCallback: VersionController.VersionCallback
    override fun setCallback(_callback: VersionController.VersionCallback) {
        versionCallback = _callback
    }

    override fun request() {
        service.getReaction().enqueue(object : Callback<List<ReactionVO>> {
            override fun onFailure(call: Call<List<ReactionVO>>, t: Throwable) {
                Log.d(TAG, "Request Fail!")
                Log.d(TAG, "message : ${t.message}")
                Thread.sleep(1000)
                request()
            }

            override fun onResponse(
                call: Call<List<ReactionVO>>,
                response: Response<List<ReactionVO>>
            ) {
                Log.d(TAG, "Request Success!")
                IO.preferenceManager.setValue("${IO.Key.REACTION}${IO.Key.VALUE}", Network.gson.toJson(response.body()))
                versionCallback.successRequest(jsonToData())
            }
        })
    }

    override fun jsonToData(): ArrayList<IO.Image> {
        val result = ArrayList<ReactionDTO>()
        val images = ArrayList<IO.Image>()
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.Key.REACTION}${IO.Key.VALUE}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, ReactionVO::class.java)
                result.add(
                    ReactionDTO(
                        "image/reaction/icon-${element.engName}.png",
                        "${element.korName}",
                        VillagerDTO.Personality.valueOf(element.source).toString(),
                        "${element.sourceNote}"
                    )
                )
                images.add(IO.Image("image/reaction", "icon-${element.engName}.png"))
            }
        }
        items = result
        return images
    }

    fun getItems(): ArrayList<ReactionDTO> {
        return items
    }
}