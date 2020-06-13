package com.example.anch_kotiln.Activity.ArtBook

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.anch_kotiln.R
import kotlinx.android.synthetic.main.activity_art_book.*

class ArtBookActivity : AppCompatActivity() {

    private fun initToolbar() {
        artBookToolbar.title = getString(R.string.title_activity_art_book)
        setSupportActionBar(artBookToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
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