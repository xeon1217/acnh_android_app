package com.example.anch_kotiln.Activity.ReactionList

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Adapter.ReactionRecyclerAdapter
import com.example.anch_kotiln.R
import kotlinx.android.synthetic.main.activity_reaction_list.*

class ReactionListActivity : AppCompatActivity() {
    val reactionBookAdapter = ReactionRecyclerAdapter(this,
        MainActivity.reactionController.getItems()
    )

    private fun initToolbar() {
        reactionListToolbar.title = getString(R.string.title_activity_reaction_list)
        setSupportActionBar(reactionListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        (menu?.findItem(R.id.app_bar_search)?.actionView as SearchView).setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    reactionBookAdapter.ListFilter().filter(query)
                    Toast.makeText(reactionBookAdapter?.context, "검색어 : ${query}, ${reactionBookAdapter?.itemCount}개 검색됨", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    reactionBookAdapter.ListFilter().filter(newText)
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
    // 툴바 관련 끝

    private fun bind() {
        reactionListRecyclerView.adapter = reactionBookAdapter
        reactionListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reaction_list)

        initToolbar()
        bind()
    }
}