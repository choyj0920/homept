package com.kuteam6.homept.loginSignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityFindPasswordBinding
import com.kuteam6.homept.restservice.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class FindPwActivity : AppCompatActivity() {

    lateinit var binding: ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivFindPw.setOnClickListener {
            val pwBackIntent = Intent(this, LoginActivity::class.java)
            startActivity(pwBackIntent)
        }

        initFindPassword()
    }
    fun maskPassword(password: String, maskChar: Char = '*', visibleChars: Int = 2): String {
        if (password.length <= visibleChars) {
            return password // 비밀번호의 길이가 visibleChars보다 작거나 같으면 그대로 반환
        }

        val masked = CharArray(password.length) { maskChar }

        password
            .takeLast(visibleChars)
            .forEachIndexed { index, char ->
                masked[password.length - visibleChars + index] = char
            }

        return String(masked)
    }


    private fun initFindPassword() {
        binding.findPwBirthEdit.apply {
            isFocusable = false
            setOnClickListener {

            }
        }

        binding.findIdImage.setOnClickListener {
            val intent = Intent(this, FindIdActivity::class.java)
            startActivity(intent)
        }

        binding.btnFindPassword.setOnClickListener {
            val id = binding.findPwIdEdit.text.toString()
            val name = binding.findPwNameEdit.text.toString()

            if(binding.findPwBirthEdit.text.toString()==""){
                return@setOnClickListener
            }
            val birth = LocalDate.parse(binding.findPwBirthEdit.text.toString())
            if(id == "" || name == ""){
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.Main){
                var result: Boolean = ApiManager.checkFindPassword(id, name, birth)

                if(result == true){


                }
            }
        }
    }
}