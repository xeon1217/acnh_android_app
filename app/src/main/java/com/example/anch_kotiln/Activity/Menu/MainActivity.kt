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
                    mainStatusTextview.text = getString(R.string.status_updating)
                    mainStatusProgressBar.isVisible = true
                    val alertDialog: AlertDialog = context.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle(getString(R.string.action_first_app_start_title))
                        builder.setMessage(getString(R.string.action_first_app_start_message))
                        builder.apply {
                            setPositiveButton(getString(R.string.receive_it),
                                DialogInterface.OnClickListener { dialog, id ->
                                    mainStatusTextviewCancelButton.isVisible = true
                                    versionController.update()
                                })
                            setNegativeButton(getString(R.string.receive_not_it),
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
                        builder.setTitle(getString(R.string.action_failure_connect_to_server_title))
                        builder.setMessage(getString(R.string.action_failure_connect_to_server_message))
                        builder.setCancelable(false)
                        builder.apply {
                            setPositiveButton(getString(R.string.retry),
                                DialogInterface.OnClickListener { dialog, id ->
                                    versionController.retryUpdate()
                                })
                            setNegativeButton(getString(R.string.do_not_it),
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
                    mainStatusTextview.text = getString(R.string.status_updating)
                    mainStatusProgressBar.isVisible = true
                    val alertDialog: AlertDialog = context.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle(getString(R.string.action_found_to_update_title))
                        builder.setMessage(getString(R.string.action_found_to_update_title))
                        builder.apply {
                            setPositiveButton(getString(R.string.do_it),
                                DialogInterface.OnClickListener { dialog, id ->
                                    mainStatusTextviewCancelButton.isVisible = true
                                    versionController.update()
                                })
                            setNegativeButton(getString(R.string.do_not_it),
                                DialogInterface.OnClickListener { dialog, id ->
                                    System.runFinalization()
                                    exitProcess(0)
                                })
                            setNeutralButton(getString(R.string.afterward_do_it),
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
                    mainStatusTextview.text = getString(R.string.status_not_require_update)
                    versionController.jsonToData()
                    finishUpdate()
                }
                Network.Status.FAIL_CONNECT_TO_SERVER -> {
                    val alertDialog: AlertDialog = context.let {
                        val builder = AlertDialog.Builder(it)
                        builder.setTitle(R.string.action_failure_connect_to_server_title)
                        builder.setMessage(R.string.action_failure_connect_to_server_message)
                        builder.setCancelable(false)
                        builder.apply {
                            setPositiveButton(getString(R.string.retry),
                                DialogInterface.OnClickListener { dialog, id ->
                                    versionController.retryUpdate()
                                })
                            setNegativeButton(getString(R.string.exit),
                                DialogInterface.OnClickListener { dialog, id ->
                                    System.runFinalization()
                                    exitProcess(0)
                                })
                            setNeutralButton(getString(R.string.afterward_do_it),
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
            builder.setTitle(getString(R.string.action_cancel_update_title))
            builder.setCancelable(false)
            builder.apply {
                setPositiveButton(getString(R.string.do_it),
                    DialogInterface.OnClickListener { dialog, id ->
                        System.runFinalization()
                        exitProcess(0)
                    })
                setNegativeButton(getString(R.string.do_not_it),
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
            getString(R.string.action_exit),
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