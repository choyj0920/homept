package com.kuteam6.homept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuteam6.homept.MypageFragment
import com.kuteam6.homept.R
import com.kuteam6.homept.RecordFragment
import com.kuteam6.homept.ScheduleFragment
import com.kuteam6.homept.SearchFragment
import com.kuteam6.homept.SnsFragment
import com.kuteam6.homept.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()
        binding.homeBnv.itemIconTintList = null
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_frm, SnsFragment())
            .commitAllowingStateLoss()

        binding.homeBnv.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.snsFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frm, SnsFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.scheduleFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frm, ScheduleFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.recordFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frm, RecordFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}