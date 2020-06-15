package com.example.anch_kotiln.Activity.VillagerList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Adapter.ItemCategoryRecyclerAdapter
import com.example.anch_kotiln.R
import kotlinx.android.synthetic.main.activity_villager_list.*

class VillagerListActivity : AppCompatActivity() {
    private lateinit var villagerListAdapter: ItemCategoryRecyclerAdapter

    //툴바 관련 시작
    private fun initToolbar() {
        villagerListToolbar.title = getString(R.string.title_activity_villager_list)
        setSupportActionBar(villagerListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        (menu?.findItem(R.id.app_bar_search)?.actionView as SearchView).setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    villagerListAdapter.ListFilter().filter(query)
                    Toast.makeText(villagerListAdapter?.context, "검색어 : ${query}, ${villagerListAdapter.getItemSize()}개 검색됨", Toast.LENGTH_SHORT).show()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    villagerListAdapter.ListFilter().filter(newText)
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
        villagerListAdapter = ItemCategoryRecyclerAdapter(this, MainActivity.villagerController.getModel(), 5)
        villagerListAdapter.setHasStableIds(true)

        villagerListRecyclerView.adapter = villagerListAdapter
        villagerListRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_villager_list)

        initToolbar()
        bind()
    }
}

//스크롤바 구현 필
