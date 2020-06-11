package com.example.anch_kotiln.Activity.ArtBook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.anch_kotiln.Model.DTO.ArtDTO
import com.example.anch_kotiln.R

class ArtBookActivity : AppCompatActivity() {
    companion object {
        val items: ArrayList<ArtDTO> = ArrayList()

        fun getItem() {

        }
    }

    fun initToolbar() {

    }

    fun bind() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_book)

        initToolbar()
        bind()
    }
}