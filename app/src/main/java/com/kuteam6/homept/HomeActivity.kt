package com.kuteam6.homept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.kuteam6.homept.trainerSearch.SearchFragment
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

        if(intent.getStringExtra("fragment") == "search") {
            binding.homeBnv.selectedItemId = R.id.searchFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_frm, SearchFragment())
                .commitAllowingStateLoss()
        }

        if(intent.getStringExtra("fragment") == "mypage") {
            binding.homeBnv.selectedItemId = R.id.mypageFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_frm, MypageFragment())
                .commitAllowingStateLoss()
        }

        binding.homeBnv.setOnItemSelectedListener {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
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
                R.id.recommendFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frm, RecommendFragment())
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
            transaction.addToBackStack(null)
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
            false
        }
    }
}