package com.example.anch_kotiln.Activity.Dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.anch_kotiln.R
import kotlinx.android.synthetic.main.dialog.*

class CustomDialog : AppCompatActivity(), Dialog {
    private val tag = CustomDialog::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setTitle(intent.getStringExtra("title"))
        setMessage(intent.getStringExtra("message"))
        setPositiveButton(intent.getStringExtra("positive"))
        setNegativeButton(intent.getStringExtra("negative"))
        setNeutralButton(intent.getStringExtra("neutral"))
    }

    override fun setTitle(text: String?) {
        if (!text.isNullOrEmpty()) {
            dialogTitle.text = text
        } else {
            dialogTitle.visibility = View.GONE
        }
    }

    override fun setMessage(text: String?) {
        if (!text.isNullOrEmpty()) {
            dialogMessage.text = text
        } else {
            dialogMessage.visibility = View.GONE
        }
    }

    override fun setPositiveButton(text: String?) {
        if (!text.isNullOrEmpty()) {
            positiveButton.text = text
            positiveButton.setOnClickListener {
                var intent = Intent()
                intent.putExtra("response", 0)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        } else {
            positiveButton.visibility = View.GONE
            positiveButton.visibility = View.GONE
        }
    }

    override fun setNegativeButton(text: String?) {
        if (!text.isNullOrEmpty()) {
            negativeButton.text = text
            negativeButton.visibility = View.VISIBLE
            negativeButtonSpace.visibility = View.VISIBLE
            negativeButton.setOnClickListener {
                intent.putExtra("response", 1)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        } else {
            negativeButton.visibility = View.GONE
            negativeButtonSpace.visibility = View.GONE
        }
    }

    override fun setNeutralButton(text: String?) {
        if (!text.isNullOrEmpty()) {
            neutralButton.text = text
            neutralButton.visibility = View.VISIBLE
            neutralButtonSpace.visibility = View.VISIBLE
            neutralButtonLayout.visibility = View.VISIBLE
            neutralButton.setOnClickListener {
                intent.putExtra("response", 2)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        } else {
            neutralButton.visibility = View.GONE
            neutralButtonSpace.visibility = View.GONE
            neutralButtonLayout.visibility = View.GONE
        }
    }
}