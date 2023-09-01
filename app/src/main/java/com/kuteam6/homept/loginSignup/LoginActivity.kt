package com.kuteam6.homept.loginSignup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.databinding.ActivityLoginBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼 이벤트
        initlogin()

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

    private fun initlogin(){
        binding.btnLogin.setOnClickListener {
            var id = binding.etLoginid.text.toString()
            var password = binding.etLoginpassword.text.toString()
            if (id == "" || password == "") { //빈값x
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var userData: UserData? = ApiManager.login(id, password); // 로그인된 Userdata
                if(userData != null){
                    //로그인에 성공한 경우 동작
                    Log.d("isTrainee", userData.isTrainee.toString())
                    Log.d("uid", userData.uid.toString())
                    UserData.userdata = userData
                    val homeIntent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(homeIntent)
                }
                else{
                    //로그인에 실패한 경우 동작
                    Toast.makeText(this@LoginActivity, "아이디 또는 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}
