package com.example.anch_kotiln.Activity.Menu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.anch_kotiln.Activity.Dialog.Dialog
import com.example.anch_kotiln.Controller.*
import com.example.anch_kotiln.R
import com.example.anch_kotiln.Utility.Common
import com.example.anch_kotiln.Utility.Network
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

/**
 *
 */

class MainActivity : AppCompatActivity() {
    private val tag = MainActivity::class.java.simpleName

    companion object {
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
                    mainStatusProgressBar.visibility = View.VISIBLE

                    intent = Intent(context, Dialog::class.java)
                    intent.putExtra(Dialog.Text.TITLE.toString(), getString(R.string.action_first_app_start_title))
                    intent.putExtra(Dialog.Text.MESSAGE.toString(), getString(R.string.action_first_app_start_message))
                    intent.putExtra(Dialog.Text.POSITIVE.toString(), getString(R.string.receive_it))
                    intent.putExtra(Dialog.Text.NEGATIVE.toString(), getString(R.string.receive_not_it))
                    startActivityForResult(intent, Network.Status.FIRST.ordinal)
                }
                Network.Status.FIRST_AND_FAIL_CONNECT_TO_SERVER -> {
                    mainStatusTextview.text = getString(R.string.status_failure_connect_to_server)

                    intent = Intent(context, Dialog::class.java)
                    intent.putExtra(Dialog.Text.TITLE.toString(), getString(R.string.action_failure_connect_to_server_title))
                    intent.putExtra(Dialog.Text.MESSAGE.toString(), getString(R.string.action_failure_connect_to_server_message))
                    intent.putExtra(Dialog.Text.POSITIVE.toString(), getString(R.string.retry))
                    intent.putExtra(Dialog.Text.NEGATIVE.toString(), getString(R.string.do_not_it))
                    startActivityForResult(intent, Network.Status.FIRST_AND_FAIL_CONNECT_TO_SERVER.ordinal)
                }
                Network.Status.REQUIRE_UPDATE -> {
                    mainStatusTextview.text = getString(R.string.status_updating)
                    mainStatusProgressBar.visibility = View.VISIBLE

                    intent = Intent(context, Dialog::class.java)
                    intent.putExtra(Dialog.Text.TITLE.toString(), getString(R.string.action_found_to_update_title))
                    intent.putExtra(Dialog.Text.MESSAGE.toString(), getString(R.string.action_found_to_update_message))
                    intent.putExtra(Dialog.Text.POSITIVE.toString(), getString(R.string.do_it))
                    intent.putExtra(Dialog.Text.NEGATIVE.toString(), getString(R.string.do_not_it))
                    intent.putExtra(Dialog.Text.NEUTRAL.toString(), getString(R.string.afterward_do_it))
                    startActivityForResult(intent, Network.Status.REQUIRE_UPDATE.ordinal)
                }
                Network.Status.NOT_REQUIRE_UPDATE -> {
                    mainStatusTextview.text = getString(R.string.status_not_require_update)
                    versionController.jsonToData()
                    finishUpdate()
                }
                Network.Status.FAIL_CONNECT_TO_SERVER -> {
                    mainStatusTextview.text = getString(R.string.status_failure_connect_to_server)

                    intent = Intent(context, Dialog::class.java)
                    intent.putExtra(Dialog.Text.TITLE.toString(), getString(R.string.action_failure_connect_to_server_title))
                    intent.putExtra(Dialog.Text.MESSAGE.toString(), getString(R.string.action_failure_connect_to_server_message))
                    intent.putExtra(Dialog.Text.POSITIVE.toString(), getString(R.string.retry))
                    intent.putExtra(Dialog.Text.NEGATIVE.toString(), getString(R.string.exit))
                    intent.putExtra(Dialog.Text.NEUTRAL.toString(), getString(R.string.afterward_do_it))
                    startActivityForResult(intent, Network.Status.FAIL_CONNECT_TO_SERVER.ordinal)
                }
                Network.Status.SUCCESS_CONNECT_TO_SERVER -> {
                }
                Network.Status.SUCCESS_UPDATE -> {
                    mainStatusTextviewCancelButton.text = getString(R.string.confirm)
                    finishUpdate()
                }
                Network.Status.CANCELED_UPDATE -> {
                    Log.d(tag, "CANCELED_UPDATE")
                    finishAffinity()
                    Common.exit()
                }
                Network.Status.ERROR_CANCELED_UPDATE -> {
                    mainStatusTextview.text = getString(R.string.status_failure_connect_to_server)

                    intent = Intent(context, Dialog::class.java)
                    intent.putExtra(Dialog.Text.TITLE.toString(), getString(R.string.action_error_cancel_update_title))
                    intent.putExtra(Dialog.Text.MESSAGE.toString(), getString(R.string.action_error_cancel_update_message))
                    intent.putExtra(Dialog.Text.POSITIVE.toString(), getString(R.string.retry))
                    intent.putExtra(Dialog.Text.NEGATIVE.toString(), getString(R.string.exit))
                    startActivityForResult(intent, Network.Status.ERROR_CANCELED_UPDATE.ordinal)
                }
                Network.Status.FAIL_CONNECT_COUNT_MAX -> {
                    mainStatusTextview.text = getString(R.string.status_failure_connect_to_server)
                    
                    intent = Intent(context, Dialog::class.java)
                    intent.putExtra(Dialog.Text.TITLE.toString(), getString(R.string.action_failure_connect_count_max_title))
                    intent.putExtra(Dialog.Text.MESSAGE.toString(), getString(R.string.action_failure_connect_count_max_message))
                    intent.putExtra(Dialog.Text.POSITIVE.toString(), getString(R.string.confirm))
                    startActivityForResult(intent, Network.Status.FAIL_CONNECT_COUNT_MAX.ordinal)
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

        override fun failureUpdate() {

        }

        override fun updateProgressBar(_progress: Int) {
            mainStatusProgressBar.progress = _progress
        }

        override fun updateProgressBar(_text: String) {
            mainStatusTextview.text = _text
        }
    }
    private val versionController = VersionController(callback)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var response = data?.getIntExtra(Dialog.Text.RESPONSE.toString(), Dialog.Button.NULL.ordinal)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                99 -> { // onCancelButtonClick, 업데이트 중 취소 버튼
                    when (response) {
                        Dialog.Button.POSITIVE.ordinal -> {
                            versionController.imageDownloader.cancel(false)
                        }
                    }
                }
                Network.Status.FIRST.ordinal -> {
                    when (response) {
                        Dialog.Button.POSITIVE.ordinal -> {
                            mainStatusTextviewCancelButton.visibility = View.VISIBLE
                            versionController.update()
                        }
                        Dialog.Button.NEGATIVE.ordinal -> Common.exit()
                    }
                }
                Network.Status.FIRST_AND_FAIL_CONNECT_TO_SERVER.ordinal -> {
                    when (response) {
                        Dialog.Button.POSITIVE.ordinal -> {
                            mainStatusTextview.text =
                                getString(R.string.status_connecting_to_server)
                            versionController.retryUpdate()
                        }
                        Dialog.Button.NEGATIVE.ordinal -> Common.exit()
                    }
                }
                Network.Status.REQUIRE_UPDATE.ordinal -> {
                    when (response) {
                        Dialog.Button.POSITIVE.ordinal -> {
                            mainStatusTextviewCancelButton.visibility = View.VISIBLE
                            versionController.update()
                        }
                        Dialog.Button.NEGATIVE.ordinal -> Common.exit()
                        Dialog.Button.NEUTRAL.ordinal -> {
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
                Network.Status.FAIL_CONNECT_TO_SERVER.ordinal -> {
                    when (response) {
                        Dialog.Button.POSITIVE.ordinal -> {
                            mainStatusTextview.text =
                                getString(R.string.status_connecting_to_server)
                            versionController.retryUpdate()
                        }
                        Dialog.Button.NEGATIVE.ordinal -> Common.exit()
                        Dialog.Button.NEUTRAL.ordinal -> {
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
                Network.Status.ERROR_CANCELED_UPDATE.ordinal -> {
                    when (response) {
                        Dialog.Button.POSITIVE.ordinal -> {
                            mainStatusTextview.text =
                                getString(R.string.status_connecting_to_server)
                            versionController.retryUpdate()
                        }
                        Dialog.Button.NEGATIVE.ordinal -> Common.exit()
                    }
                }
                Network.Status.FAIL_CONNECT_COUNT_MAX.ordinal -> {
                    when (response) {
                        Dialog.Button.POSITIVE.ordinal -> Common.exit()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        onCancelButtonClick(View(this))
        return
    }

    override fun onDestroy() {
        super.onDestroy()
        if(versionController.imageDownloader.status == AsyncTask.Status.RUNNING) {
            versionController.imageDownloader.cancel(true)
        }
        Log.d(tag, "destory!")
    }

    fun onCancelButtonClick(v: View) {
        var intent = Intent(this, Dialog::class.java)
        intent.putExtra(Dialog.Text.TITLE.toString(), getString(R.string.action_cancel_update_title))
        intent.putExtra(Dialog.Text.POSITIVE.toString(), getString(R.string.yes))
        intent.putExtra(Dialog.Text.NEGATIVE.toString(), getString(R.string.no))
        startActivityForResult(intent, 99)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callback.setContext(this)
        versionController.isUpdate()

        Log.d(tag, "onCreate!")
    }

    interface Callback {
        fun setContext(_context: Context)
        fun connect(status: Network.Status)
        fun finishUpdate()
        fun failureUpdate()
        fun updateProgressBar(_progress: Int)
        fun updateProgressBar(_text: String)
    }
}