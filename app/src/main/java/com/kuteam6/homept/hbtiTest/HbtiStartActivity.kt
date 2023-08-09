package com.kuteam6.homept.hbtiTest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuteam6.homept.databinding.ActivityHbtiStartBinding

class HbtiStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHbtiStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHbtiStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartTest.setOnClickListener {
            val intent = Intent(this, HbtiTestActivity::class.java)
            startActivity(intent)
        }
    }
}