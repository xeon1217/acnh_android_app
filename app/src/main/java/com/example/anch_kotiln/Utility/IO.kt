package com.example.anch_kotiln.Utility

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import com.example.anch_kotiln.Activity.Menu.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.net.ConnectException
import java.net.URL

class IO : Application() {
    companion object {
        private val tag = IO::class.java.simpleName
        lateinit var preferenceManager: PreferenceManager
        lateinit var file: File
    }

    override fun onCreate() {
        preferenceManager = PreferenceManager(applicationContext)
        file = filesDir
        super.onCreate()
    }

    class ImageDownloader(var callback: MainActivity.Callback) :
        AsyncTask<List<Image>, Int, Int>() {
        override fun doInBackground(vararg imageNames: List<Image>): Int {
            var bitmap: Bitmap
            var imagePath: File = file
            var fos: FileOutputStream
            var count = 0f
            var temp = 0

            try {
                Log.d(tag, "${imageNames.size}")
                imageNames.forEach { images ->
                    Log.d(tag, "${images.size}")
                    images.forEach { image ->
                        if (isCancelled) {
                            preferenceManager.setValue(
                                Key.EXCEPTION_UPDATE_DATA.toString(),
                                "$imagePath"
                            )
                            Log.d(tag, "Cancel!")
                            Log.d(
                                tag,
                                "last Write File : ${preferenceManager.getValue(Key.EXCEPTION_UPDATE_DATA.toString())}"
                            )
                            return Network.Status.CANCELED_UPDATE.ordinal
                        }
                        imagePath = File("$file/${image.imagePath}/${image.imageName}")
                        if (!File("$file/${image.imagePath}").exists()) {
                            File("$file/${image.imagePath}").mkdirs()
                        }
                        if (!imagePath.exists()) {
                            imagePath.createNewFile()
                            fos = FileOutputStream(imagePath)
                            bitmap =
                                BitmapFactory.decodeStream(URL("${Network.BASE_URL}/${image.imagePath}/${image.imageName}").openStream())
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                        }
                        if ((count++ / images.size * 100).toInt() != temp) {
                            temp = (count / images.size * 100).toInt()
                            onProgressUpdate(temp)
                        }
                    }
                }
            } catch (e: Exception) {
                preferenceManager.setValue(Key.EXCEPTION_UPDATE_DATA.toString(), "$imagePath")
                Log.d(tag, "Exception!")
                Log.d(tag, "last Write File : ${preferenceManager.getValue(Key.EXCEPTION_UPDATE_DATA.toString())}")
                return Network.Status.ERROR_CANCELED_UPDATE.ordinal
            }
            return Network.Status.SUCCESS_UPDATE.ordinal
        }

        override fun onCancelled(result: Int) {
            Log.d(tag, "$isCancelled :: $result")
            when (result) {
                Network.Status.CANCELED_UPDATE.ordinal -> callback.connect(Network.Status.CANCELED_UPDATE)
                Network.Status.ERROR_CANCELED_UPDATE.ordinal -> callback.connect(Network.Status.ERROR_CANCELED_UPDATE)
            }
            super.onCancelled(result)
        }

        override fun onCancelled() {
            Log.d(tag, "$isCancelled")
            super.onCancelled()
        }

        override fun onPostExecute(result: Int) {
            if (preferenceManager.getValue(Key.EXCEPTION_UPDATE_DATA.toString()).isNullOrEmpty() && !isCancelled) {
                callback.connect(Network.Status.SUCCESS_UPDATE)
                Log.d(tag, "finish")
            } else {
                callback.connect(Network.Status.ERROR_CANCELED_UPDATE)
                Log.d(tag, "cancel")
            }
            super.onPostExecute(result)
        }

        override fun onProgressUpdate(vararg values: Int?) {
            values[0]?.let { callback.updateProgressBar(it) }
            Log.d(tag, "${values[0]}")
            super.onProgressUpdate(*values)
        }
    }

    data class Image(
        val imagePath: String,
        val imageName: String
    )

    enum class Key(private val key: String) {
        // 테이블 조회 관련 Key
        table("table"), // 버전 조회용
        VALUE("value"), // 값 조회용

        // 상태 관련 Key
        EXCEPTION_UPDATE("exception_update"), // 업데이트 중 오류 발생
        FINISH_UPDATE("finish_update"), // 업데이트가 정상적으로 되었는지?
        FIRST("is_first"), // 처음으로 앱을 실행했는지?

        EXCEPTION_UPDATE_DATA("exception_update_data"), // 업데이트 중 오류 발생

        // 테이블 이름 관련 Key
        VERSION("version"),
        VILLAGER("villager"),
        FISH("fish"),
        INSECT("insect"),
        REACTION("reaction"),
        ART("art");

        override fun toString(): String {
            return key
        }
    }
}