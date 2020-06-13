package com.example.anch_kotiln.Activity.FossilBook

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.anch_kotiln.R
import kotlinx.android.synthetic.main.activity_fossil_book.*

class FossilBookActivity : AppCompatActivity() {
    private fun initToolbar() {
        fossilBookToolbar.title = getString(R.string.title_activity_fossil_book)
        setSupportActionBar(fossilBookToolbar)
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
        setContentView(R.layout.activity_fossil_book)

        initToolbar()
        bind()
    }
}