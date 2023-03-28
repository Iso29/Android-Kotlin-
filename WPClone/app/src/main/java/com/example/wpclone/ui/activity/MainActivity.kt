package com.example.wpclone.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.wpclone.R
import com.example.wpclone.ui.adapter.TabNavigationAdapter
import com.example.wpclone.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Runnable

class MainActivity : AppCompatActivity() {
    private lateinit var tabNavAdapter : TabNavigationAdapter
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()


    }

    private fun onClick(){

    }
    private fun init(){
        tabNavAdapter = TabNavigationAdapter(supportFragmentManager,lifecycle)
        setSupportActionBar(binding.toolBarMain)
        binding.apply {
            tabLayoutMain.addTab(tabLayoutMain.newTab().setText("Chats"))
            tabLayoutMain.addTab(tabLayoutMain.newTab().setText("Calls"))
            tabLayoutMain.addTab(tabLayoutMain.newTab().setText("Status"))
            viewPagerMain.adapter = tabNavAdapter

            tabLayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPagerMain.currentItem = tab!!.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            viewPagerMain.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    changeFabIcon(position)
                    tabLayoutMain.selectTab(tabLayoutMain.getTabAt(position))
                }
            })


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        when (id) {
            R.id.menu_search -> Toast.makeText(applicationContext,"Search",Toast.LENGTH_SHORT).show()
            R.id.action_new_group -> Toast.makeText(applicationContext,"new Group",Toast.LENGTH_SHORT).show()
            R.id.action_new_broadcast -> Toast.makeText(applicationContext,"new broadcast",Toast.LENGTH_SHORT).show()
            R.id.action_wp_web -> Toast.makeText(applicationContext,"web wp",Toast.LENGTH_SHORT).show()
            R.id.action_starred_messagge -> Toast.makeText(applicationContext,"starred messages",Toast.LENGTH_SHORT).show()
            R.id.action_settings -> startActivity(Intent(this,SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeFabIcon(index :Int){
        binding.floatingActionButton.hide()
        Handler().postDelayed(object : Runnable {
            override fun run() {
                when(index){
                    0 -> {binding.floatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_round_chat_24))
                        binding.floatingActionButton.setOnClickListener {
                            startActivity(Intent(this@MainActivity,ContactsActivity::class.java))
                        }
                    }
                    1 -> binding.floatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_call_24))
                    2 -> binding.floatingActionButton.setImageDrawable(resources.getDrawable(R.drawable.ic_round_camera_alt_24))
                }
                binding.floatingActionButton.show()
            }
        },300)
    }
}

