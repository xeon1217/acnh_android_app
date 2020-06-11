package com.example.anch_kotiln.Activity.CreatureBook

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.anch_kotiln.Model.DTO.InsectDTO
import com.example.anch_kotiln.R
import com.example.anch_kotiln.Utility.IO
import kotlinx.android.synthetic.main.activity_creature_book_insect_popup.*
import kotlinx.android.synthetic.main.activity_villager_list_popup.*

class CreatureBookInsectPopupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_creature_book_insect_popup)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val item = intent.getSerializableExtra("item") as InsectDTO

        insect_book_popup_toolbar.title = item.name
        insect_image_view.setImageBitmap(BitmapFactory.decodeFile("${IO.file.path}/${item.imageCritterpediaResource}"))
        insect_date_text_view.text = item.dateText
        insect_time_text_view.text = item.timeText
        insect_location_text_view.text = "${item.where} ${item.weather}"
        insect_price_text_view.text = "${item.sellPrice}벨"
    }

    fun onClickCancelButton(v: View) {
        finish()
    }
}