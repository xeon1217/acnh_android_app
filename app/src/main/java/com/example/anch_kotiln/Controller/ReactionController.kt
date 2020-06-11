package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.acnh.Model.ReactionVO
import com.example.anch_kotiln.Activity.Menu.MainActivity
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
    private val key = "reaction_value"
    private val items = ArrayList<ReactionDTO>()
    private val images = ArrayList<IO.Image>()
    private var finished = false
    lateinit var versionCallback: VersionController.VersionCallback
    override fun setCallback(_callback: VersionController.VersionCallback) {
        versionCallback = _callback
    }

    override fun request() {
        service.getReaction().enqueue(object : Callback<List<ReactionVO>> {
            override fun onFailure(call: Call<List<ReactionVO>>, t: Throwable) {
                Log.d(TAG, "Request Fail!")
                Log.d(TAG, "message : ${t.message}")
            }

            override fun onResponse(
                call: Call<List<ReactionVO>>,
                response: Response<List<ReactionVO>>
            ) {
                Log.d(TAG, "Request Success!")
                IO.preferenceManager.setValue("${IO.key.reaction}${IO.key.value}", Network.gson.toJson(response.body()))
                jsonToData()
                versionCallback.successRequest(images)
            }
        })
    }


    override fun jsonToData() {
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.key.reaction}${IO.key.value}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, ReactionVO::class.java)
                items.add(
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
    }

    override fun getImageNames(): ArrayList<IO.Image> {
        return images
    }

    override fun isFinished() : Boolean{
        return finished
    }

    fun getItems(): ArrayList<ReactionDTO> {
        return items
    }
}