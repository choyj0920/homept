package com.kuteam6.homept.loginSignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivityFindIdBinding

class FindIdActivity : AppCompatActivity() {

    lateinit var binding: ActivityFindIdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.findPwImage.setOnClickListener {
            val intent = Intent(this, FindPwActivity::class.java)
            startActivity(intent)
        }

        binding.ivFindId.setOnClickListener {
            val idBackIntent = Intent(this, LoginActivity::class.java)
            startActivity(idBackIntent)
        }
    }
}