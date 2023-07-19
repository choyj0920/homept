package com.kuteam6.homept.loginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuteam6.homept.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼 이벤트
        binding.loginBtn.setOnClickListener {

            val id = binding.idEdit.text.toString()
            val password = binding.pwEdit.text.toString()
        }

        //아이디 찾기 버튼 이벤트
        binding.findIdBtn.setOnClickListener {
            val findIdIntent: Intent = Intent(this@LoginActivity, FindIdActivity::class.java)
            startActivity(findIdIntent)
        }

        //비밀번호 찾기 버튼 이벤트
        binding.findPwBtn.setOnClickListener {
            val findPwIntent: Intent = Intent(this@LoginActivity, FindPwActivity::class.java)
            startActivity(findPwIntent)
        }

        //회원가입 버튼 이벤트
        binding.signUpBtn.setOnClickListener {
            val signUpIntent: Intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

}
