package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivityMypageInfoAlarmBinding


class MypageInfoAlarmActivity: AppCompatActivity() {

    lateinit var binding: ActivityMypageInfoAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageInfoAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "푸시 알림 설정"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }
    }
}