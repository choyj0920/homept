package com.kuteam6.homept

import ChatFragment
import HomeFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.kuteam6.homept.trainerSearch.SearchFragment
import com.kuteam6.homept.databinding.ActivityHomeBinding
import com.kuteam6.homept.hbtiTest.HbtiStartActivity
import com.kuteam6.homept.myPage.MypageFragment
import com.kuteam6.homept.restservice.data.UserData
import com.kuteam6.homept.sns.SnsFragment
import com.kuteam6.homept.trainerRecommend.RecommendFragment

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!UserData.userdata?.isTrainee!!) {
            if (UserData.userdata?.hbti == null) {
                val hbtiIntent = Intent(this, HbtiStartActivity::class.java)
                startActivity(hbtiIntent)
            }
        }

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

        if(intent.getStringExtra("fragment") == "recommend") {
            binding.homeBnv.selectedItemId = R.id.recommendFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_frm, RecommendFragment())
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
//                R.id.chatFragment -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.home_frm, ChatFragment())
//                        .commitAllowingStateLoss()
//                    return@setOnItemSelectedListener true
//                }
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_frm, HomeFragment())
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