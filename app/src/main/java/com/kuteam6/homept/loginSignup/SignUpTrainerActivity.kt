package com.kuteam6.homept.loginSignup

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivitySignupTrainerBinding
import com.kuteam6.homept.restservice.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpTrainerActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupTrainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 중복 체크 구현
        binding.checkIdBtn.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var checkid=binding.trainerIdEdit.text.toString()
                binding.checkId.setText("load...");
                var result = ApiManager.checkIdDupicated(checkid);
                binding.checkId.setText("${ if(result)"아이디중복" else "아이디중복x"}")

            }

        }

        binding.btnSubmit.setOnClickListener {
            val trainerProfileIntent = Intent(this@SignUpTrainerActivity, TrainerProfileActivity::class.java)
            trainerProfileIntent.putExtra("name", binding.trainerNameEdit.text.toString())
            trainerProfileIntent.putExtra("id", binding.trainerIdEdit.text.toString())
            trainerProfileIntent.putExtra("pwd", binding.trainerPwEdit.text.toString())
            if (binding.genderFemale.isChecked) {
                trainerProfileIntent.putExtra("gender", "f")
            } else {
                trainerProfileIntent.putExtra("gender", "m")
            }
            trainerProfileIntent.putExtra("birth", binding.trainerBirthDateEdit.text.toString())

            startActivity(trainerProfileIntent)
        }

        val passwordEditText3 = binding.trainerPwEdit

        passwordEditText3.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                passwordEditText3.transformationMethod = null
            }
            else{
                passwordEditText3.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        val passwordEditText4 = binding.trainerPwCheckEdit

        passwordEditText4.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                passwordEditText4.transformationMethod = null
            }
            else{
                passwordEditText4.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //트레이너로 잘못 들어간 경우 뒤로가기
        binding.trainerCloseIv.setOnClickListener {
            val trainerSignUpBackIntent = Intent(this, SignUpActivity::class.java)
            startActivity(trainerSignUpBackIntent)
        }
    }
}