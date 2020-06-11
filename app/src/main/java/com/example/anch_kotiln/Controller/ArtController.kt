package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Model.DTO.ArtDTO
import com.example.anch_kotiln.Utility.Network
import com.example.anch_kotiln.Service.ArtService
import com.example.anch_kotiln.Model.VO.ArtVO
import com.example.anch_kotiln.Model.VO.VersionVO
import com.example.anch_kotiln.Service.VersionService
import com.example.anch_kotiln.Utility.IO
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtController() : Controller {
    private val TAG = ArtController::class.java.simpleName
    private val service = Network.instance.create(ArtService::class.java)
    private val key = "art_value"
    private val items = ArrayList<ArtDTO>()
    private val images = ArrayList<IO.Image>()
    private var finished = false
    lateinit var versionCallback: VersionController.VersionCallback
    override fun setCallback(_callback: VersionController.VersionCallback) {
        versionCallback = _callback
    }

    override fun request() {
        service.getArts().enqueue(object : Callback<List<ArtVO>> {
            override fun onFailure(call: Call<List<ArtVO>>, t: Throwable) {
                Log.d(TAG, "Request Fail!")
                Log.d(TAG, "message : ${t.message}")
            }

            override fun onResponse(
                call: Call<List<ArtVO>>,
                response: Response<List<ArtVO>>
            ) {
                Log.d(TAG, "Request Success!")
                IO.preferenceManager.setValue("${IO.key.art}${IO.key.value}", Network.gson.toJson(response.body()))
                jsonToData()
                versionCallback.successRequest(images)
            }
        })
    }

    override fun jsonToData() {
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.key.art}${IO.key.value}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, ArtVO::class.java)
                var dto = ArtDTO(
                    "image/art/${element.engName}.png",
                    "image/art/${element.engName}-fake.png",
                    element.korName,
                    element.realArtworkTitle,
                    element.artist,
                    element.museumDescription,
                    element.size,
                    element.existFake
                )
                items.add(dto)
                images.add(IO.Image("image/art", "${element.engName}.png"))
                if (dto.existFake) {
                    images.add(IO.Image("image/art", "${element.engName}-fake.png"))
                }
            }
        }
    }

    override fun getImageNames(): ArrayList<IO.Image> {
        return images
    }

    override fun isFinished(): Boolean {
        return finished
    }


    fun getModel() {

    }
}