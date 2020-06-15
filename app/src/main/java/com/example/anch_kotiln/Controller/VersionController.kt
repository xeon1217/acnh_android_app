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
import java.io.File

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

        override fun retry() {
            images.clear()
        }
    }

    fun retryUpdate() {
        versionCallback.retry()
        isUpdate(mCallback)
    }

    fun isUpdate(_callback: MainActivity.Callback) {
        mCallback = _callback
        setCallback()
        service.getAllVersions().enqueue(object : Callback<List<VersionVO>> {
            override fun onFailure(call: Call<List<VersionVO>>, t: Throwable) { //연결 실패
                Log.d(tag, "Request Fail!")
                Log.d(tag, "message : ${t.message}")
                mCallback.updateProgressBar("서버와 연결하는데 실패했습니다.")
                if (!IO.preferenceManager.getBoolean(IO.Key.FIRST.toString())) {
                    mCallback.connect(Network.Status.FIRST_AND_FAIL_CONNECT_TO_SERVER)
                } else {
                    mCallback.connect(Network.Status.FAIL_CONNECT_TO_SERVER)
                }
            }

            override fun onResponse(
                call: Call<List<VersionVO>>,
                response: Response<List<VersionVO>>
            ) { //연결 성공
                Log.d(tag, "Request Success!")
                Log.d(tag, "response value : ${response.body()}")
                Log.d(tag, "response length : ${response.body()?.size}")
                Log.d(tag, "response errorBody : ${response.errorBody()}")
                Log.d(tag, "response massage : ${response.message()}")
                Log.d(tag, "response code : ${response.code()}")
                Log.d(tag, "response url : ${response.raw().request().url()}")

                IO.preferenceManager.setValue(IO.Key.VERSION.toString(), Network.gson.toJson(response.body()))
                Log.d(tag, "${IO.preferenceManager.getValue(IO.Key.VERSION.toString())}")

                val versionArray =
                    JsonParser().parse(IO.preferenceManager.getValue(IO.Key.VERSION.toString())).asJsonArray
                if (!versionArray.isJsonNull) {
                    versionArray.forEach {
                        var vo = Network.gson.fromJson(it, VersionVO::class.java)
                        if ((IO.preferenceManager.getVersion(vo.tableName) != vo.version) or (!IO.preferenceManager.getBoolean(
                                "${vo.tableName}${IO.Key.FINISH_UPDATE}"
                            ))
                        ) {
                            foundUpdate++
                        }
                        Log.d(tag, "Table Name : ${vo.tableName}")
                        Log.d(
                            tag,
                            "Version : ${IO.preferenceManager.getVersion(vo.tableName)} :: ${vo.version}"
                        )
                        Log.d(
                            tag,
                            "require Update : ${!IO.preferenceManager.getBoolean("${vo.tableName}${IO.Key.FINISH_UPDATE}")}"
                        )
                    }
                }

                mCallback.updateProgressBar("서버와 연결하는데 성공했습니다.")

                if(IO.preferenceManager.getBoolean(IO.Key.EXCEPTION_UPDATE.toString())) { // 업데이트 중 오류 발견
                    Log.d(tag, "Found Exception File :: ${IO.preferenceManager.getValue(IO.Key.EXCEPTION_UPDATE_DATA.toString())}")
                    if(File(IO.preferenceManager.getValue(IO.Key.EXCEPTION_UPDATE_DATA.toString())).exists()) {
                        File(IO.preferenceManager.getValue(IO.Key.EXCEPTION_UPDATE_DATA.toString())).delete()
                        IO.preferenceManager.removeValue(IO.Key.EXCEPTION_UPDATE.toString())
                        IO.preferenceManager.removeValue(IO.Key.EXCEPTION_UPDATE_DATA.toString())
                        Log.d(tag, "Exception File Delete")
                    }
                }

                if (!IO.preferenceManager.getBoolean(IO.Key.FIRST.toString())) {
                    mCallback.connect(Network.Status.FIRST) // 최초 기동
                } else {
                    if (foundUpdate > 0) {
                        mCallback.connect(Network.Status.REQUIRE_UPDATE) //업데이트가 있음
                    } else {
                        mCallback.connect(Network.Status.NOT_REQUIRE_UPDATE) //업데이트가 없음
                    }
                }
            }
        })
    }

    fun update() {
        val versionArray =
            JsonParser().parse(IO.preferenceManager.getValue(IO.Key.VERSION.toString())).asJsonArray
        if (!versionArray.isJsonNull) {
            versionArray.forEach {
                var vo = Network.gson.fromJson(it, VersionVO::class.java)
                if ((IO.preferenceManager.getVersion(vo.tableName) != vo.version) or (!IO.preferenceManager.getBoolean(
                        "${vo.tableName}${IO.Key.FINISH_UPDATE}"
                    ))
                ) {
                    Log.d(tag, vo.tableName)
                    requireUpdate("${vo.tableName}${IO.Key.VALUE}")
                } else {
                    notRequireUpdate("${vo.tableName}${IO.Key.VALUE}")
                }
            }
        }
    }

    fun jsonToData() {
        val versionArray =
            JsonParser().parse(IO.preferenceManager.getValue(IO.Key.VERSION.toString())).asJsonArray
        if (!versionArray.isJsonNull) {
            versionArray.forEach {
                var vo = Network.gson.fromJson(it, VersionVO::class.java)
                notRequireUpdate("${vo.tableName}${IO.Key.VALUE}")
            }
        }
    }

    fun finishUpdate() {
        IO.preferenceManager.setBoolean(IO.Key.FIRST.toString(), true)
        val versionArray =
            JsonParser().parse(IO.preferenceManager.getValue(IO.Key.VERSION.toString())).asJsonArray
        if (!versionArray.isJsonNull) {
            versionArray.forEach {
                var vo = Network.gson.fromJson(it, VersionVO::class.java)
                IO.preferenceManager.setVersion(vo.tableName, vo.version)
                IO.preferenceManager.setBoolean("${vo.tableName}${IO.Key.FINISH_UPDATE}", true)
            }
        }
    }


    private fun setCallback() {
        MainActivity.villagerController.setCallback(versionCallback)
        MainActivity.artController.setCallback(versionCallback)
        MainActivity.creatureController.fishController.setCallback(versionCallback)
        MainActivity.creatureController.insectController.setCallback(versionCallback)
        MainActivity.reactionController.setCallback(versionCallback)
    }

    private fun requireUpdate(tableName: String) {
        when (tableName) {
            "${IO.Key.VILLAGER}${IO.Key.VALUE}" -> MainActivity.villagerController.request()
            "${IO.Key.ART}${IO.Key.VALUE}" -> MainActivity.artController.request()
            "${IO.Key.FISH}${IO.Key.VALUE}" -> MainActivity.creatureController.fishController.request()
            "${IO.Key.INSECT}${IO.Key.VALUE}" -> MainActivity.creatureController.insectController.request()
            "${IO.Key.REACTION}${IO.Key.VALUE}" -> MainActivity.reactionController.request()
        }
    }

    private fun notRequireUpdate(tableName: String) {
        when (tableName) {
            "${IO.Key.VILLAGER}${IO.Key.VALUE}" -> MainActivity.villagerController.jsonToData()
            "${IO.Key.ART}${IO.Key.VALUE}" -> MainActivity.artController.jsonToData()
            "${IO.Key.FISH}${IO.Key.VALUE}" -> MainActivity.creatureController.fishController.jsonToData()
            "${IO.Key.INSECT}${IO.Key.VALUE}" -> MainActivity.creatureController.insectController.jsonToData()
            "${IO.Key.REACTION}${IO.Key.VALUE}" -> MainActivity.reactionController.jsonToData()
        }
    }

    interface VersionCallback {
        fun successRequest(_images: ArrayList<IO.Image>)
        fun retry()
    }
}