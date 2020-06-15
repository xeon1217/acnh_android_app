package com.example.anch_kotiln.Activity.CreatureBook

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anch_kotiln.Activity.Menu.MainActivity
import com.example.anch_kotiln.Adapter.ItemCategoryRecyclerAdapter
import com.example.anch_kotiln.Utility.Common
import com.example.anch_kotiln.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_creature_book.*

class CreatureBookActivity : AppCompatActivity() {
    private lateinit var realtimeAdapter: ItemCategoryRecyclerAdapter
    private lateinit var insectAdapter: ItemCategoryRecyclerAdapter
    private lateinit var fishAdapter: ItemCategoryRecyclerAdapter
    private var tabPostion: Int = 0

    //툴바 관련 시작
    private fun initToolbar() {
        creatureBookToolbar.title =
            "${getString(R.string.title_activity_realtime_book)} (${Common.getCurrentDate()}월, ${Common.getCurrentTimeStr()}시 기준)"
        setSupportActionBar(creatureBookToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        (menu?.findItem(R.id.app_bar_search)?.actionView as SearchView).setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    when (tabPostion) {
                        0 -> {
                            realtimeAdapter.ListFilter().filter(query)
                            Toast.makeText(
                                realtimeAdapter.context,
                                "검색어 : ${query}, ${realtimeAdapter.getItemSize()}개 검색됨",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        1 -> {
                            insectAdapter.ListFilter().filter(query)
                            Toast.makeText(
                                insectAdapter.context,
                                "검색어 : ${query}, ${insectAdapter.getItemSize()}개 검색됨",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        2 -> {
                            fishAdapter.ListFilter().filter(query)
                            Toast.makeText(
                                fishAdapter.context,
                                "검색어 : ${query}, ${fishAdapter.getItemSize()}개 검색됨",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    when (tabPostion) {
                        0 -> realtimeAdapter.ListFilter().filter(newText)
                        1 -> insectAdapter.ListFilter().filter(newText)
                        2 -> fishAdapter.ListFilter().filter(newText)
                    }
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
    //툴바 관련 끝

    private fun bind() {
        //각 객체별 어댑터와 bind
        realtimeAdapter = ItemCategoryRecyclerAdapter(
            this,
            MainActivity.creatureController.getModel()
        ,5)
        realtimeAdapter.setHasStableIds(true)
        insectAdapter = ItemCategoryRecyclerAdapter(
            this,
            MainActivity.creatureController.insectController.getModel()
        ,5)
        insectAdapter.setHasStableIds(true)
        fishAdapter = ItemCategoryRecyclerAdapter(
            this,
            MainActivity.creatureController.fishController.getModel()
        , 5)
        fishAdapter.setHasStableIds(true)

        realtimeRecyclerView.adapter = realtimeAdapter
        realtimeRecyclerView.layoutManager = LinearLayoutManager(this)

        insectRecyclerView.adapter = insectAdapter
        insectRecyclerView.layoutManager = LinearLayoutManager(this)

        fishRecyclerView.adapter = fishAdapter
        fishRecyclerView.layoutManager = LinearLayoutManager(this)

        creatureBookTabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) { // 탭이 다시 선택 되었을 때
            }

            override fun onTabUnselected(tab: TabLayout.Tab) { // 탭이 선택 해제 되었을 때,
                tabPostion = tab.position
                when (tabPostion) {
                    0 -> realtimeRecyclerView.isVisible = false // 실시간 도감
                    1 -> insectRecyclerView.isVisible = false // 곤충 도감
                    2 -> fishRecyclerView.isVisible = false // 물고기 도감
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab) { // 탭이 선택 되었을 때,
                tabPostion = tab.position

                when (tabPostion) {
                    0 -> { // 실시간 도감
                        realtimeRecyclerView.isVisible = true
                        creatureBookToolbar.title =
                            "${getString(R.string.title_activity_realtime_book)} (${Common.getCurrentDate()}월, ${Common.getCurrentTimeStr()}시 기준)"
                    }
                    1 -> { // 곤충 도감
                        insectRecyclerView.isVisible = true
                        creatureBookToolbar.title = getString(R.string.title_activity_insect_book)
                    }
                    2 -> { // 물고기 도감
                        fishRecyclerView.isVisible = true
                        creatureBookToolbar.title = getString(R.string.title_activity_fish_book)
                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creature_book)

        //creatureDateSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.date_array))
        creatureDateSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.date_array)
        )
        creatureTimeSpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.time_array)
        )
        initToolbar()
        bind()
    }
}