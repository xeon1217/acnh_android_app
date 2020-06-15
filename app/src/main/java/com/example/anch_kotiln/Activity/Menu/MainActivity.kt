package com.example.anch_kotiln.Activity.Menu

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.anch_kotiln.Activity.Dialog.CustomDialog
import com.example.anch_kotiln.Controller.*
import com.example.anch_kotiln.R
import com.example.anch_kotiln.Utility.Network
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

/**
 *
 */

class MainActivity : AppCompatActivity() {
    private val tag = MainActivity::class.java.simpleName
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
            var intent: Intent
            when (status) {
                Network.Status.FIRST -> {
                    mainStatusTextview.text = getString(R.string.status_updating)
                    mainStatusProgressBar.isVisible = true

                    intent = Intent(context, CustomDialog::class.java)
                    intent.putExtra("title", getString(R.string.action_first_app_start_title))
                    intent.putExtra("message", getString(R.string.action_first_app_start_message))
                    intent.putExtra("positive", getString(R.string.receive_it))
                    intent.putExtra("negative", getString(R.string.receive_not_it))
                    startActivityForResult(intent, Network.Status.FIRST.getValue())
                }
                Network.Status.FIRST_AND_FAIL_CONNECT_TO_SERVER -> {
                    mainStatusTextview.text = getString(R.string.status_failure_connect_to_server)

                    intent = Intent(context, CustomDialog::class.java)
                    intent.putExtra(
                        "title",
                        getString(R.string.action_failure_connect_to_server_title)
                    )
                    intent.putExtra(
                        "message",
                        getString(R.string.action_failure_connect_to_server_message)
                    )
                    intent.putExtra("positive", getString(R.string.retry))
                    intent.putExtra("negative", getString(R.string.do_not_it))
                    startActivityForResult(
                        intent,
                        Network.Status.FIRST_AND_FAIL_CONNECT_TO_SERVER.getValue()
                    )
                }
                Network.Status.REQUIRE_UPDATE -> {
                    mainStatusTextview.text = getString(R.string.status_updating)
                    mainStatusProgressBar.isVisible = true

                    intent = Intent(context, CustomDialog::class.java)
                    intent.putExtra("title", getString(R.string.action_found_to_update_title))
                    intent.putExtra("message", getString(R.string.action_found_to_update_message))
                    intent.putExtra("positive", getString(R.string.do_it))
                    intent.putExtra("negative", getString(R.string.do_not_it))
                    intent.putExtra("neutral", getString(R.string.afterward_do_it))
                    startActivityForResult(intent, Network.Status.REQUIRE_UPDATE.getValue())
                }
                Network.Status.NOT_REQUIRE_UPDATE -> {
                    mainStatusTextview.text = getString(R.string.status_not_require_update)
                    versionController.jsonToData()
                    finishUpdate()
                }
                Network.Status.FAIL_CONNECT_TO_SERVER -> {
                    mainStatusTextview.text = getString(R.string.status_failure_connect_to_server)

                    intent = Intent(context, CustomDialog::class.java)
                    intent.putExtra(
                        "title",
                        getString(R.string.action_failure_connect_to_server_title)
                    )
                    intent.putExtra(
                        "message",
                        getString(R.string.action_failure_connect_to_server_message)
                    )
                    intent.putExtra("positive", getString(R.string.retry))
                    intent.putExtra("negative", getString(R.string.exit))
                    intent.putExtra("neutral", getString(R.string.afterward_do_it))
                    startActivityForResult(intent, Network.Status.FAIL_CONNECT_TO_SERVER.getValue())
                }
                Network.Status.SUCCESS_CONNECT_TO_SERVER -> {
                }
                Network.Status.SUCCESS_UPDATE -> {
                    mainStatusTextviewCancelButton.text = getString(R.string.confirm)
                }
                Network.Status.CANCELED_UPDATE -> {
                }
                Network.Status.ERROR_CANCELED_UPDATE -> {
                    mainStatusTextview.text = getString(R.string.status_failure_connect_to_server)

                    intent = Intent(context, CustomDialog::class.java)
                    intent.putExtra("title", getString(R.string.action_error_cancel_update_title))
                    intent.putExtra(
                        "message",
                        getString(R.string.action_error_cancel_update_message)
                    )
                    intent.putExtra("positive", getString(R.string.retry))
                    intent.putExtra("negative", getString(R.string.exit))
                    startActivityForResult(intent, Network.Status.ERROR_CANCELED_UPDATE.getValue())
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var response = data?.getIntExtra("response", -1)
        Log.d(tag, "request :: $requestCode")
        Log.d(tag, "result :: $resultCode")
        Log.d(tag, "response :: $response")
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                99 -> { // onCancelButtonClick, 업데이트 중 취소 버튼
                    when (response) {
                        0 -> exit()
                    }
                }
                Network.Status.FIRST.getValue() -> {
                    when (response) {
                        0 -> {
                            mainStatusTextviewCancelButton.isVisible = true
                            versionController.update()
                        }
                        1 -> exit()
                    }
                }
                Network.Status.FIRST_AND_FAIL_CONNECT_TO_SERVER.getValue() -> {
                    when (response) {
                        0 -> {
                            mainStatusTextview.text =
                                getString(R.string.status_connecting_to_server)
                            versionController.retryUpdate()
                        }
                        1 -> exit()
                    }
                }
                Network.Status.REQUIRE_UPDATE.getValue() -> {
                    when (response) {
                        0 -> {
                            mainStatusTextviewCancelButton.isVisible = true
                            versionController.update()
                        }
                        1 -> exit()
                        2 -> {
                            versionController.jsonToData()
                            startActivity(
                                Intent(
                                    this,
                                    MenuActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            )
                        }
                    }
                }
                Network.Status.FAIL_CONNECT_TO_SERVER.getValue() -> {
                    when (response) {
                        0 -> {
                            mainStatusTextview.text =
                                getString(R.string.status_connecting_to_server)
                            versionController.retryUpdate()
                        }
                        1 -> exit()
                        2 -> {
                            versionController.jsonToData()
                            startActivity(
                                Intent(
                                    this,
                                    MenuActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            )
                        }
                    }
                }
                Network.Status.ERROR_CANCELED_UPDATE.getValue() -> {
                    when (response) {
                        0 -> {
                            mainStatusTextview.text =
                                getString(R.string.status_connecting_to_server)
                            versionController.retryUpdate()
                        }
                        1 -> exit()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
            finishAffinity()
            System.runFinalization()
            exitProcess(0)
            return
        }
        //'뒤로' 버튼 한번 클릭 시 메시지
        Toast.makeText(
            this,
            getString(R.string.action_exit),
            Toast.LENGTH_SHORT
        ).show()
        //lastTimeBackPressed에 '뒤로'버튼이 눌린 시간을 기록
        lastTimeBackPressed = System.currentTimeMillis()
        return
    }

    fun onCancelButtonClick(v: View) {
        var intent = Intent(this, CustomDialog::class.java)
        intent.putExtra("title", getString(R.string.action_cancel_update_title))
        intent.putExtra("positive", getString(R.string.yes))
        intent.putExtra("negative", getString(R.string.no))
        startActivityForResult(intent, 99)
    }

    private fun exit() {
        System.runFinalization()
        exitProcess(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callback.setContext(this)
        versionController.isUpdate(callback)

        Log.d(tag, "onCreate!")
    }

    interface Callback {
        fun setContext(_context: Context)
        fun connect(status: Network.Status)
        fun finishUpdate()
        fun updateProgressBar(_progress: Int)
        fun updateProgressBar(_text: String)
    }
}