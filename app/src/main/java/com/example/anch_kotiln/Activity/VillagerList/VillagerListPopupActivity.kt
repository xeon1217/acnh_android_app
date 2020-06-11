package com.example.anch_kotiln.Activity.VillagerList

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.anch_kotiln.Model.DTO.VillagerDTO
import com.example.anch_kotiln.R
import com.example.anch_kotiln.Utility.IO
import kotlinx.android.synthetic.main.activity_villager_list_popup.*

class VillagerListPopupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_villager_list_popup)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val item = intent.getSerializableExtra("item") as VillagerDTO

        villagerListPopupToolbar.title = "${item.name} ${item.gender}"
        villagerListPopupImageView.setImageBitmap(BitmapFactory.decodeFile("${IO.file.path}/${item.imageFullResource}"))
        villagerListPopupPersonalityTextView.text = item.personality.toString()
        villagerListPopupSpeciesTextView.text = item.species.toString()
        villagerListPopupBirthdayTextView.text = item.birthdayText
        villagerListPopupHobbyTextView.text = item.hobby.toString()
        villagerListPopupCatchphraseTextView.text = item.catchPhrase
        villagerListPopupFavoritePhraseTextView.text = "\"${item.favoritePhrase}\""
    }

    fun onClickCancelButton(v: View) {
        finish()
    }
}