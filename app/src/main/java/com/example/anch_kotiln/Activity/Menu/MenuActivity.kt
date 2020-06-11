package com.example.anch_kotiln.Activity.Menu

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.anch_kotiln.Activity.ArtBook.ArtBookActivity
import com.example.anch_kotiln.Activity.CreatureBook.CreatureBookActivity
import com.example.anch_kotiln.Activity.FossilBook.FossilBookActivity
import com.example.anch_kotiln.Activity.ReactionList.ReactionListActivity
import com.example.anch_kotiln.Activity.VillagerList.VillagerListActivity
import com.example.anch_kotiln.R
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.drawer_menu.*

class MenuActivity : AppCompatActivity() {
    private var lastTimeBackPressed: Long = 0
    private val TAG = MenuActivity::class.java.simpleName

    fun initToolbar() {
        setSupportActionBar(menuToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp) // 홈버튼 이미지 변경
    }

    fun initNavigation() {
        drawerNavigationView.setNavigationItemSelectedListener { mItem ->
            when (mItem.itemId) {
                R.id.villagerList -> onCreateVillagerListActivity()
                else -> onClickUnimplementedFunction(null)
            }
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawer_menu.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun onClickUnimplementedFunction(v: View?) {
        Toast.makeText(this, getString(R.string.unimplemented_function), Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (drawer_menu.isDrawerOpen(GravityCompat.START)) {
            drawer_menu.closeDrawer(GravityCompat.START)
        } else {
            if (System.currentTimeMillis() - lastTimeBackPressed < 2000) {
                finishAffinity()
                System.runFinalization()
                System.exit(0)
                return
            }
            //'뒤로' 버튼 한번 클릭 시 메시지
            Toast.makeText(this, "'${getString(R.string.back)}' 버튼을 한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            //lastTimeBackPressed에 '뒤로'버튼이 눌린 시간을 기록
            lastTimeBackPressed = System.currentTimeMillis()
            return
        }
    }

    private fun onCreateVillagerListActivity() {
        startActivity(Intent(this, VillagerListActivity::class.java))
    }

    private fun onCreateCreatureBookActivity() {
        startActivity(Intent(this, CreatureBookActivity::class.java))
    }

    private fun onCreateReactionListActivity() {
        startActivity(Intent(this, ReactionListActivity::class.java))
    }

    private fun onCreateArtBookActivity() {
        startActivity(Intent(this, ArtBookActivity::class.java))
    }

    private fun onCreateFossilBookActivity() {
        startActivity(Intent(this, FossilBookActivity::class.java))
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_menu)

        initToolbar()
        initNavigation()

        villagerListButton.setOnClickListener {
            onCreateVillagerListActivity();
        }

        creatureBookButton.setOnClickListener {
            onCreateCreatureBookActivity()
        }

        reactionListButton.setOnClickListener {
            onCreateReactionListActivity()
        }

        artBookButton.setOnClickListener {
            onCreateArtBookActivity()
        }

        fossilBookButton.setOnClickListener {
            onCreateFossilBookActivity()
        }
    }
}