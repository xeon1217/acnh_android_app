package com.example.anch_kotiln.Utility

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.widget.ProgressBar
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Adapter.CategoryRecyclerAdapter
import com.example.anch_kotiln.R
import retrofit2.http.Url
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class IO : Application() {
    companion object {
        private val TAG = IO::class.java.simpleName
        lateinit var preferenceManager: PreferenceManager
        lateinit var file: File
        val key = Key()
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
            var imagePath: File
            var fos: FileOutputStream
            var count = 0f
            var temp = 0

            Log.d(TAG, "${imageNames.size}")
            imageNames.forEach { images ->
                Log.d(TAG, "${images.size}")
                images.forEach { image ->
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
            return 0
        }

        override fun onCancelled(result: Int?) {
            super.onCancelled(result)
        }

        override fun onCancelled() {
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

    class Key {
        // 테이블 조회 관련 Key
        val table = "table" // 버전 조회용
        val value = "value" // 값 조회용

        // 상태 관련 Key
        val finishUpdate = "finish_update" // 업데이트가 정상적으로 되었는지?
        val first = "is_first" // 처음으로 앱을 실행했는지?

        // 테이블 이름 관련 Key
        var version = "version"
        var villager = "villager"
        var fish = "fish"
        var insect = "insect"
        var reaction = "reaction"
        var art = "art"
    }
}