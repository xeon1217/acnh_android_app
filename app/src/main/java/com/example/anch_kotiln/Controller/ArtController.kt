package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Model.DTO.ArtDTO
import com.example.anch_kotiln.Model.DTO.ModelDTO
import com.example.anch_kotiln.Model.DTO.ObjectDTO
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

class ArtController : Controller {
    private val TAG = ArtController::class.java.simpleName
    private val service = Network.instance.create(ArtService::class.java)
    private lateinit var items: ArrayList<ArtDTO>
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
                IO.preferenceManager.setValue("${IO.Key.ART}${IO.Key.VALUE}", Network.gson.toJson(response.body()))
                versionCallback.successRequest(jsonToData())
            }
        })
    }

    override fun jsonToData(): ArrayList<IO.Image> {
        val result = ArrayList<ArtDTO>()
        val images = ArrayList<IO.Image>()
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.Key.ART}${IO.Key.VALUE}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, ArtVO::class.java)
                var dto = ArtDTO(
                    "image/art/${element.engName}.png",
                    "image/art/${element.engName}-fake.png",
                    element.korName,
                    element.realArtworkTitle,
                    element.artist,
                    element.fakeDescription,
                    element.size,
                    element.existFake
                )
                result.add(dto)
                images.add(IO.Image("image/art", "${element.engName}.png"))
                if (dto.existFake) {
                    images.add(IO.Image("image/art", "${element.engName}-fake.png"))
                }
            }
        }
        items = result
        return images
    }

    fun getModel(): ArrayList<ModelDTO> {
        val result: ArrayList<ModelDTO> = ArrayList(0)
        val existFakeArts: ArrayList<ArtDTO> = ArrayList(0)
        val noExistFakeArts: ArrayList<ArtDTO> = ArrayList(0)

        items.forEach { dto ->
            if(dto.existFake) {
                existFakeArts.add(dto)
            } else {
                noExistFakeArts.add(dto)
            }
        }
        result.add(ModelDTO(existFakeArts as ArrayList<ObjectDTO>))
        result.add(ModelDTO(noExistFakeArts as ArrayList<ObjectDTO>))
        return result
    }
}