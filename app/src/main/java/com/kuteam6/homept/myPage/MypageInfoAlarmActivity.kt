package com.kuteam6.homept.myPage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivityMypageInfoAlarmBinding


class MypageInfoAlarmActivity: AppCompatActivity() {

    lateinit var binding: ActivityMypageInfoAlarmBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageInfoAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "푸시 알림 설정"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences("alarm_prefereneces", Context.MODE_PRIVATE)

        binding.switchPushNotifications.isChecked = sharedPreferences.getBoolean("notifications_enabled", true)
        
        binding.switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("notifications_enabled",isChecked).apply()
        }
    }
}