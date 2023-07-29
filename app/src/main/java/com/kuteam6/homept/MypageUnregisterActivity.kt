package com.kuteam6.homept

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityMypageInfoBinding
import com.kuteam6.homept.databinding.ActivityMypageUnregisterBinding
import com.kuteam6.homept.loginSignup.LoginActivity
import com.kuteam6.homept.restservice.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MypageUnregisterActivity: AppCompatActivity() {

    lateinit var binding: ActivityMypageUnregisterBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMypageUnregisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "회원 탈퇴"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            val searchTrainerIntent = Intent(this, MypageInfoActivity::class.java)
            startActivity(searchTrainerIntent)
        }

        initUnregister()
    }

    private fun initUnregister(){
        binding.btnUnregister.setOnClickListener {
            var id = binding.etUnregisterid.text.toString()
            var password = binding.etUnregisterpassword.text.toString()
            if (id == "" || password == "") {
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.Main) {
                var result:Boolean = ApiManager.unRegister(id, password)

                if(result){
                    binding.tvUnregisterResult.text = "탈퇴 완료"
                    //로그아웃 처리 및 로그인 화면으로 이동
                }
                else{
                    binding.tvUnregisterResult.text = "탈퇴 안됨"
                }
            }
        }
    }
}