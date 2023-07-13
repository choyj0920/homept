package com.kuteam6.homept

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivitySignupTraineeBinding

class SignUpTraineeActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupTraineeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTraineeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            val TraineeProfileIntent = Intent(this@SignUpTraineeActivity, TraineeProfileActivity::class.java)
            startActivity(TraineeProfileIntent)
        }
    }
}