package com.example.anch_kotiln.Activity.CreatureBook

import android.app.Activity
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.anch_kotiln.Model.DTO.FishDTO
import com.example.anch_kotiln.R
import com.example.anch_kotiln.Utility.IO
import kotlinx.android.synthetic.main.activity_creature_book_fish_popup.*

class CreatureBookFishPopupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_creature_book_fish_popup)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val item = intent.getSerializableExtra("item") as FishDTO

        fishBookPopupToolbar.title = item.name
        fishBookPopupImageVew.setImageBitmap(BitmapFactory.decodeFile("${IO.file.path}/${item.imageCritterpediaResource}"))
        fishBookPopupDateTextView.text = item.dateText
        fishBookPopupTimeTextView.text = item.timeText
        fishBookPopupLocationTextView.text = item.where.toString()
        if(item.fin) {
            fishBookPopupSizeTextView.text = "${item.shadow} (지느러미 있음)"
        } else {
            fishBookPopupSizeTextView.text = "${item.shadow}"
        }
        fishBookPopupPriceTextView.text = "${item.sellPrice}벨"
    }

    fun onClickCancelButton(v: View) {
        finish()
    }
}