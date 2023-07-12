package com.kuteam6.homept

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivitySignupTrainerBinding

class SignUpTrainerActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupTrainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}