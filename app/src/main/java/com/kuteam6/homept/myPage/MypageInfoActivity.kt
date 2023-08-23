package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.databinding.ActivityMypageInfoBinding
import com.kuteam6.homept.loginSignup.LoginActivity
import com.kuteam6.homept.restservice.data.UserData

class MypageInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMypageInfoBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMypageInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "회원 정보"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        // 회원 이름
        val user_name = UserData.userdata?.name.toString()
        binding.tvMyPageInfoName.text = user_name

        // 전화번호

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

        //로그아웃 버튼 이벤트
        binding.btnMyPageLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }


        //회원 탈퇴 버튼 이벤트
        binding.btnMypageInfoUnregister.setOnClickListener {
            val unregisterIntent = Intent(this, MypageUnregisterActivity::class.java)
            startActivity(unregisterIntent)
        }

    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그아웃")
        builder.setMessage("로그아웃하시겠습니까?")

        //"예"버튼을 누르면 로그아웃하는 기능
        builder.setPositiveButton("예"){_,_->
            //로그아웃 구현 코드 추가

            //이후 로그인 화면으로 이동
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }

        //"아니요"버튼을 누르면 대화 상자 닫기
        builder.setNegativeButton("아니요"){dialog,_->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}