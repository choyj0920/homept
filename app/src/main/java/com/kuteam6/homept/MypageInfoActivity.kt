package com.kuteam6.homept

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivityMypageInfoBinding

class MypageInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMypageInfoBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMypageInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Back 버튼 이벤트
        binding.btnMyPageInfoBack.setOnClickListener {
            val backIntent = Intent(this, MypageFragment::class.java)
            startActivity(backIntent)
        }

        //비밀 번호 변경 버튼 이벤트
        binding.btnMyPageChangePw.setOnClickListener {
            val changePwIntent = Intent(this, MypageChangePwActivity::class.java)
            startActivity(changePwIntent)
        }

        //푸시 알림 설정 버튼 이벤트
        binding.btnMyPageAlarm.setOnClickListener {
            val AlarmIntent = Intent(this, MypageInfoAlarmActivity::class.java)
            startActivity(AlarmIntent)
        }

        //회원 탈퇴 버튼 이벤트
        binding.btnMypageInfoUnregister.setOnClickListener {
            val unregisterIntent = Intent(this, MypageUnregisterActivity::class.java)
            startActivity(unregisterIntent)
        }

    }
}