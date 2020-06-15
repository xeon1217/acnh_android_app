package com.example.anch_kotiln.Activity.ArtBook

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Adapter.ArtRecyclerAdapter
import com.example.anch_kotiln.Adapter.ItemCategoryRecyclerAdapter
import com.example.anch_kotiln.Adapter.ReactionRecyclerAdapter
import com.example.anch_kotiln.Controller.ArtController
import com.example.anch_kotiln.R
import kotlinx.android.synthetic.main.activity_art_book.*

class ArtBookActivity : AppCompatActivity() {
    private lateinit var artAdapter: ItemCategoryRecyclerAdapter

    private fun initToolbar() {
        artBookToolbar.title = getString(R.string.title_activity_art_book)
        setSupportActionBar(artBookToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        (menu?.findItem(R.id.app_bar_search)?.actionView as SearchView).setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    artAdapter.ListFilter().filter(query)
                    Toast.makeText(
                        artAdapter.context,
                        "검색어 : ${query}, ${artAdapter.getItemSize()}개 검색됨",
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    artAdapter.ListFilter().filter(newText)
                    return false
                }
            })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun bind() {
        artAdapter = ItemCategoryRecyclerAdapter(this, MainActivity.artController.getModel(), 1)
        artAdapter.setHasStableIds(true)

        artBookRecyclerView.adapter = artAdapter
        artBookRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_art_book)

        initToolbar()
        bind()
    }
}