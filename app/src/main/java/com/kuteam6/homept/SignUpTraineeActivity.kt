package com.kuteam6.homept

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivitySignupTraineeBinding

class SignUpTraineeActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupTraineeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTraineeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}