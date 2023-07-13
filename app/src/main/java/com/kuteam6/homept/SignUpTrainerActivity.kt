package com.kuteam6.homept

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivitySignupTrainerBinding

class SignUpTrainerActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupTrainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            val TrainerProfileIntent = Intent(this@SignUpTrainerActivity, TrainerProfileActivity::class.java)
            startActivity(TrainerProfileIntent)
        }
    }
}