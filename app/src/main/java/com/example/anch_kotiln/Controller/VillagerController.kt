package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Model.DTO.ModelDTO
import com.example.anch_kotiln.Model.DTO.ObjectDTO
import com.example.anch_kotiln.Model.DTO.VillagerDTO
import com.example.anch_kotiln.Model.VO.VersionVO
import com.example.anch_kotiln.Utility.Network
import com.example.anch_kotiln.Service.VillagerService
import com.example.anch_kotiln.Model.VO.VillagerVO
import com.example.anch_kotiln.Service.VersionService
import com.example.anch_kotiln.Utility.IO
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VillagerController : Controller {
    private val TAG = VillagerController::class.java.simpleName
    private val service = Network.instance.create(VillagerService::class.java)
    private val items = ArrayList<VillagerDTO>()
    private val images = ArrayList<IO.Image>()
    private var finished = false
    lateinit var versionCallback: VersionController.VersionCallback
    override fun setCallback(_callback: VersionController.VersionCallback) {
        versionCallback = _callback
    }

    override fun request() {
        service.getVillagers().enqueue(object : Callback<List<VillagerVO>> {
            override fun onFailure(call: Call<List<VillagerVO>>, t: Throwable) {
                Log.d(TAG, "Request Fail!")
                Log.d(TAG, "message : ${t.message}")
            }

            override fun onResponse(call: Call<List<VillagerVO>>, response: Response<List<VillagerVO>>) {
                Log.d(TAG, "Request Success!")
                IO.preferenceManager.setValue("${IO.key.villager}${IO.key.value}", Network.gson.toJson(response.body()))
                jsonToData()
                versionCallback.successRequest(images)
            }
        })
    }

    override fun jsonToData() {
        val jsonElement = JsonParser().parse(IO.preferenceManager.getValue("${IO.key.villager}${IO.key.value}"))
        if (!jsonElement.isJsonNull) {
            jsonElement.asJsonArray.forEach {
                var element = Network.gson.fromJson(it, VillagerVO::class.java)
                var dto = VillagerDTO(
                    "image/villager/icon-${element.engName}.png",
                    "image/villager/full-${element.engName}.png",
                    element.korName,
                    VillagerDTO.Personality.valueOf(element.personality),
                    VillagerDTO.Species.valueOf(element.species),
                    VillagerDTO.Gender.valueOf(element.gender),
                    element.birthday,
                    VillagerDTO.Hobby.valueOf(element.hobby),
                    element.catchphrase,
                    element.favoritePhrase,
                    ""
                )
                items.add(dto)
                images.add(IO.Image("image/villager", "full-${element.engName}.png"))
                images.add(IO.Image("image/villager", "icon-${element.engName}.png"))
            }
        }
    }

    override fun getImageNames(): ArrayList<IO.Image> {
        return images
    }

    override fun isFinished(): Boolean {
        return finished
    }

    fun getModel(): ArrayList<ModelDTO> {
        val result: ArrayList<ModelDTO> = ArrayList()
        for (i in VillagerDTO.Species.values().indices) {
            var temp = getSpecies(i)
            if (temp.size > 0) {
                result.add(ModelDTO(temp))
            }
        }
        return result
    }

    fun getSpecies(species: Int): ArrayList<ObjectDTO> {
        val result: ArrayList<ObjectDTO> = ArrayList()
        for (i in items.indices) {
            if (items[i].species.ordinal == species) {
                Log.d(null, "getSpecies, items[$i].species.ordinal : ${items[i].species.ordinal}")
                result.add(items[i])
                Log.d(null, "getSpecies, items[$i].species.ordinal : ${items[i].species}")
            }
        }
        return result
    }
}