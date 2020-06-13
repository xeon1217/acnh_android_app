package com.example.anch_kotiln.Activity.Menu

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.anch_kotiln.Controller.*
import com.example.anch_kotiln.R
import com.example.anch_kotiln.Utility.Network
import kotlinx.android.synthetic.main.activity_main.*
import java.util.zip.Inflater
import kotlin.system.exitProcess

/**
 *
 */

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private var lastTimeBackPressed: Long = 0

    companion object {
        val versionController = VersionController()
        val villagerController = VillagerController()
        val creatureController = CreatureController()
        val reactionController = ReactionController()
        val artController = ArtController()
    }

    private val callback: Callback = object : Callback {
        private lateinit var context: Context

        override fun setContext(_context: Context) {
            context = _context
        }

        override fun connect(status: Network.Status) {
            when (status) {
                Network.Status.FIRST -> {
                    mainStatusTextview.text = "업데이트 중입니다."
                    mainStatusProgressBar.isVisible = true
                    val alertDialog: AlertDialog = context.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle(status.toString())
                        builder.setMessage("데이터를 업데이트해야 합니다.\n데이터를 받아올까요?")
                        builder.apply {
                            setPositiveButton("받기",
                                DialogInterface.OnClickListener { dialog, id ->
                                    mainStatusTextviewCancelButton.isVisible = true
                                    versionController.update()
                                })
                            setNegativeButton("종료",
                                DialogInterface.OnClickListener { dialog, id ->
                                    System.runFinalization()
                                    exitProcess(0)
                                })
                        }
                        builder.create()
                    }
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                Network.Status.FIRST_AND_FAIL_CONNECT_TO_SERVER -> {
                    val alertDialog: AlertDialog = context.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle("업데이트 서버와 연결하지 못했습니다.")
                        builder.setMessage("다시 시도할까요?")
                        builder.setCancelable(false)
                        builder.apply {
                            setPositiveButton("다시 시도",
                                DialogInterface.OnClickListener { dialog, id ->
                                    versionController.retryUpdate()
                                })
                            setNegativeButton("종료",
                                DialogInterface.OnClickListener { dialog, id ->
                                    System.runFinalization()
                                    exitProcess(0)
                                })
                        }
                        builder.create()
                    }
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                Network.Status.REQUIRE_UPDATE -> {
                    mainStatusTextview.text = "업데이트 중입니다."
                    mainStatusProgressBar.isVisible = true
                    val alertDialog: AlertDialog = context.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle("업데이트가 있습니다.")
                        builder.setMessage("업데이트 할까요?")
                        builder.apply {
                            setPositiveButton("하기",
                                DialogInterface.OnClickListener { dialog, id ->
                                    mainStatusTextviewCancelButton.isVisible = true
                                    versionController.update()
                                })
                            setNegativeButton("하지 않기",
                                DialogInterface.OnClickListener { dialog, id ->
                                    System.runFinalization()
                                    exitProcess(0)
                                })
                            setNeutralButton("나중에 하기",
                                DialogInterface.OnClickListener { dialog, which ->
                                    versionController.jsonToData()
                                    startActivity(
                                        Intent(
                                            context,
                                            MenuActivity::class.java
                                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    )
                                })
                        }
                        builder.create()
                    }
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                Network.Status.NOT_REQUIRE_UPDATE -> {
                    mainStatusTextview.text = "업데이트가 필요 없어요"
                    versionController.jsonToData()
                    finishUpdate()
                }
                Network.Status.FAIL_CONNECT_TO_SERVER -> {
                    val alertDialog: AlertDialog = context.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle("업데이트 서버와 연결하지 못했습니다.")
                        builder.setMessage("다시 시도할까요?")
                        builder.setCancelable(false)
                        builder.apply {
                            setPositiveButton("다시 시도",
                                DialogInterface.OnClickListener { dialog, id ->
                                    versionController.retryUpdate()
                                })
                            setNegativeButton("종료",
                                DialogInterface.OnClickListener { dialog, id ->
                                    System.runFinalization()
                                    exitProcess(0)
                                })
                            setNeutralButton("나중에 하기",
                                DialogInterface.OnClickListener { dialog, which ->
                                    versionController.jsonToData()
                                    startActivity(
                                        Intent(
                                            context,
                                            MenuActivity::class.java
                                        ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    )
                                })
                        }
                        builder.create()
                    }
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
                Network.Status.SUCCESS_CONNECT_TO_SERVER -> {
                }
            }
        }

        override fun finishUpdate() {
            versionController.finishUpdate()
            startActivity(
                Intent(context, MenuActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }

        override fun updateProgressBar(_progress: Int) {
            mainStatusProgressBar.progress = _progress
        }

        override fun updateProgressBar(_text: String) {
            mainStatusTextview.text = _text
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callback.setContext(this)
        versionController.isUpdate(callback)

        Log.d(TAG, "onCreate!")
    }

    override fun onBackPressed() {
        exit()
    }

    fun onCancelButtonClick(v: View) {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("업데이트를 취소할까요?")
            builder.setCancelable(false)
            builder.apply {
                setPositiveButton("취소할래",
                    DialogInterface.OnClickListener { dialog, id ->
                        System.runFinalization()
                        exitProcess(0)
                    })
                setNegativeButton("잘못 눌렀어",
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            }
            builder.create()
        }
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun exit() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
            finishAffinity()
            System.runFinalization()
            exitProcess(0)
            return
        }
        //'뒤로' 버튼 한번 클릭 시 메시지
        Toast.makeText(
            this,
            "'${getString(R.string.back)}' 버튼을 한 번 더 누르시면 앱이 종료됩니다.",
            Toast.LENGTH_SHORT
        ).show()
        //lastTimeBackPressed에 '뒤로'버튼이 눌린 시간을 기록
        lastTimeBackPressed = System.currentTimeMillis()
        return
    }

    interface Callback {
        fun setContext(_context: Context)
        fun connect(status: Network.Status)
        fun finishUpdate()
        fun updateProgressBar(_progress: Int)
        fun updateProgressBar(_text: String)
    }
}