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
        private val TAG = IO::class.java.simpleName
        lateinit var preferenceManager: PreferenceManager
        lateinit var file: File
    }

    override fun onCreate() {
        preferenceManager = PreferenceManager(applicationContext)
        file = filesDir
        super.onCreate()
    }

    class ImageLoader() : AsyncTask<ImageView, Void, Void>() {
        override fun doInBackground(vararg params: ImageView): Void? {
            return null
        }
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
                Log.d(TAG, "${imageNames.size}")
                imageNames.forEach { images ->
                    Log.d(TAG, "${images.size}")
                    images.forEach { image ->
                        if(isCancelled) {
                            return 1
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
                        if((count++ / images.size * 100).toInt() != temp) {
                            temp = (count / images.size * 100).toInt()
                            onProgressUpdate(temp)
                        }
                    }
                }
            } catch (e: ConnectException) {
                cancel(true)
                preferenceManager.setBoolean(Key.EXCEPTION_UPDATE.toString(), true)
                preferenceManager.setValue(Key.EXCEPTION_UPDATE_DATA.toString(), "$imagePath")
                return -1
            } catch (e: Exception) {
                Log.d(TAG, "$e")
            }
            return 0
        }

        override fun onCancelled(result: Int?) {
            Log.d(TAG, "$isCancelled :: $result")
            when(result) {
                -1 -> callback.connect(Network.Status.ERROR_CANCELED_UPDATE)
                0 -> callback.connect(Network.Status.SUCCESS_UPDATE)
                1 -> callback.connect(Network.Status.CANCELED_UPDATE)
            }
            super.onCancelled(result)
        }

        override fun onCancelled() {
            Log.d(TAG, "$isCancelled")
            super.onCancelled()
        }

        override fun onPostExecute(result: Int?) {
            if (isCancelled) {
                preferenceManager.removeValue("finish_update")
                Log.d(TAG, "cancel")
            } else {
                callback.finishUpdate()
                Log.d(TAG, "finish")
            }
            super.onPostExecute(result)
        }

        override fun onProgressUpdate(vararg values: Int?) {
            values[0]?.let { callback.updateProgressBar(it) }
            Log.d(TAG, "${values[0]}")
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