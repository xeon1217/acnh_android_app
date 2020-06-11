package com.example.anch_kotiln.Controller

import android.util.Log
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Model.VO.VersionVO
import com.example.anch_kotiln.Service.VersionService
import com.example.anch_kotiln.Utility.IO
import com.example.anch_kotiln.Utility.Network
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VersionController {
    private val tag = VersionController::class.java.simpleName
    private val service = Network.instance.create(VersionService::class.java)
    private var foundUpdate = 0
    lateinit var mCallback: MainActivity.Callback
    private val versionCallback: VersionCallback = object : VersionCallback {
        private var finish = 0
        private val images = ArrayList<IO.Image>()
        override fun successRequest(_images: ArrayList<IO.Image>) {
            images.addAll(_images)
            if (foundUpdate == ++finish) {
                IO.ImageDownloader(mCallback).execute(images)
            }
        }

        override fun failRequest() {

        }
    }

    fun retryUpdate() {
        isUpdate(mCallback)
    }

    fun isUpdate(_callback: MainActivity.Callback) {
        mCallback = _callback
        setCallback()
        service.getAllVersions().enqueue(object : Callback<List<VersionVO>> {
            override fun onFailure(call: Call<List<VersionVO>>, t: Throwable) {
                Log.d(tag, "Request Fail!")
                Log.d(tag, "message : ${t.message}")
                //연결 실패
                if(!IO.preferenceManager.getBoolean(IO.key.first)) {
                    mCallback.firstUpdate()
                } else {
                    mCallback.failureConnectServer()
                }
            }

            override fun onResponse(
                call: Call<List<VersionVO>>,
                response: Response<List<VersionVO>>
            ) {
                Log.d(tag, "Request Success!")
                Log.d(tag, "response value : ${response.body()}")
                Log.d(tag, "response length : ${response.body()?.size}")
                Log.d(tag, "response errorBody : ${response.errorBody()}")
                Log.d(tag, "response massage : ${response.message()}")
                Log.d(tag, "response code : ${response.code()}")
                Log.d(tag, "response url : ${response.raw().request().url()}")
                //연결 성공

                mCallback.updateProgressBar("서버와 연결하는데 성공했습니다.")

                IO.preferenceManager.setValue(IO.key.version, Network.gson.toJson(response.body()))
                Log.d(tag, IO.preferenceManager.getValue(IO.key.version))

                val versionArray =
                    JsonParser().parse(IO.preferenceManager.getValue(IO.key.version)).asJsonArray
                if (!versionArray.isJsonNull) {
                    versionArray.forEach {
                        var vo = Network.gson.fromJson(it, VersionVO::class.java)
                        if ((IO.preferenceManager.getVersion("${vo.tableName}") != vo.version) or (!IO.preferenceManager.getBoolean(
                                "${vo.tableName}${IO.key.finishUpdate}"
                            ))
                        ) {
                            foundUpdate++
                        }
                        Log.d(tag, "Table Name : ${vo.tableName}")
                        Log.d(
                            tag,
                            "Version : ${IO.preferenceManager.getVersion("${vo.tableName}")} :: ${vo.version}"
                        )
                        Log.d(
                            tag,
                            "require Update : ${!IO.preferenceManager.getBoolean("${vo.tableName}${IO.key.finishUpdate}")}"
                        )
                    }
                }

                if(!IO.preferenceManager.getBoolean(IO.key.first)) {
                    mCallback.firstUpdate()
                } else {
                    if (foundUpdate > 0) { //업데이트가 있음
                        mCallback.foundUpdate()
                    } else { //업데이트가 없음
                        mCallback.notFoundUpdate()
                    }
                }
            }
        })
    }

    fun update() {
        val versionArray =
            JsonParser().parse(IO.preferenceManager.getValue(IO.key.version)).asJsonArray
        if (!versionArray.isJsonNull) {
            versionArray.forEach {
                var vo = Network.gson.fromJson(it, VersionVO::class.java)
                if ((IO.preferenceManager.getVersion("${vo.tableName}") != vo.version) or (!IO.preferenceManager.getBoolean(
                        "${vo.tableName}${IO.key.finishUpdate}"
                    ))
                ) {
                    Log.d(tag, vo.tableName)
                    requireUpdate("${vo.tableName}${IO.key.value}")
                } else {
                    notRequireUpdate("${vo.tableName}${IO.key.value}")
                }
            }
        }
    }

    fun jsonToData() {
        val versionArray =
            JsonParser().parse(IO.preferenceManager.getValue(IO.key.version)).asJsonArray
        if (!versionArray.isJsonNull) {
            versionArray.forEach {
                var vo = Network.gson.fromJson(it, VersionVO::class.java)
                notRequireUpdate("${vo.tableName}${IO.key.value}")
            }
        }
    }

    fun finishUpdate() {
        IO.preferenceManager.setBoolean(IO.key.first, true)
        val versionArray =
            JsonParser().parse(IO.preferenceManager.getValue(IO.key.version)).asJsonArray
        if (!versionArray.isJsonNull) {
            versionArray.forEach {
                var vo = Network.gson.fromJson(it, VersionVO::class.java)
                IO.preferenceManager.setVersion("${vo.tableName}", vo.version)
                IO.preferenceManager.setBoolean("${vo.tableName}${IO.key.finishUpdate}", true)
            }
        }
    }


    private fun setCallback() {
        MainActivity.villagerController.setCallback(versionCallback)
        MainActivity.artController.setCallback(versionCallback)
        MainActivity.creatureController.setCallback(versionCallback)
        MainActivity.reactionController.setCallback(versionCallback)
    }

    private fun requireUpdate(tableName: String) {
        when (tableName) {
            "${IO.key.villager}${IO.key.value}" -> MainActivity.villagerController.request()
            "${IO.key.art}${IO.key.value}" -> MainActivity.artController.request()
            "${IO.key.fish}${IO.key.value}" -> MainActivity.creatureController.requestFish()
            "${IO.key.insect}${IO.key.value}" -> MainActivity.creatureController.requestInsect()
            "${IO.key.reaction}${IO.key.value}" -> MainActivity.reactionController.request()
        }
    }

    private fun notRequireUpdate(tableName: String) {
        when (tableName) {
            "${IO.key.villager}${IO.key.value}" -> MainActivity.villagerController.jsonToData()
            "${IO.key.art}${IO.key.value}" -> MainActivity.artController.jsonToData()
            "${IO.key.fish}${IO.key.value}" -> MainActivity.creatureController.jsonToFish()
            "${IO.key.insect}${IO.key.value}" -> MainActivity.creatureController.jsonToInsect()
            "${IO.key.reaction}${IO.key.value}" -> MainActivity.reactionController.jsonToData()
        }
    }

    interface VersionCallback {
        fun successRequest(_images: ArrayList<IO.Image>)
        fun failRequest()
    }
}