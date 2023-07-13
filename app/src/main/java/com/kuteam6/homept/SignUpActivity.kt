package com.kuteam6.homept

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //트레이너 가입 버튼 이벤트
        binding.trainerSignupBtn.setOnClickListener{
            val trainerSignUpIntent = Intent(this@SignUpActivity, SignUpTrainerActivity::class.java)
            startActivity(trainerSignUpIntent)
        }

        //트레이니 가입 버튼 이벤트
        binding.traineeSignupBtn.setOnClickListener {
            val traineeSignUpIntent = Intent(this@SignUpActivity, SignUpTraineeActivity::class.java)
            startActivity(traineeSignUpIntent)
        }
    }
}