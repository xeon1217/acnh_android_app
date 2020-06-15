package com.example.anch_kotiln.Activity.Dialog

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.anch_kotiln.R
import kotlinx.android.synthetic.main.dialog.*

class Dialog : AppCompatActivity(), DialogInterface {
    private val tag = Dialog::class.java.simpleName

    enum class Button {
        NULL,
        POSITIVE,
        NEGATIVE,
        NEUTRAL;
    }

    enum class Text(val value: String){
        TITLE("title"),
        MESSAGE("message"),
        POSITIVE("positive"),
        NEGATIVE("negative"),
        NEUTRAL("neutral"),
        RESPONSE("response");

        override fun toString(): String {
            return value
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setTitle(intent.getStringExtra(Text.TITLE.toString()))
        setMessage(intent.getStringExtra(Text.MESSAGE.toString()))
        setPositiveButton(intent.getStringExtra(Text.POSITIVE.toString()))
        setNegativeButton(intent.getStringExtra(Text.NEGATIVE.toString()))
        setNeutralButton(intent.getStringExtra(Text.NEUTRAL.toString()))
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
                intent.putExtra(Text.RESPONSE.toString(), Button.POSITIVE.ordinal)
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
                intent.putExtra(Text.RESPONSE.toString(), Button.NEGATIVE.ordinal)
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
                intent.putExtra(Text.RESPONSE.toString(), Button.NEUTRAL.ordinal)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        } else {
            neutralButton.visibility = View.GONE
            neutralButtonSpace.visibility = View.GONE
            neutralButtonLayout.visibility = View.GONE
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_OUTSIDE) {
            return false
        }
        return true
    }

    override fun onBackPressed() {
        return
    }
}