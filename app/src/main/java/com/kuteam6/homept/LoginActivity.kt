package com.kuteam6.homept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuteam6.homept.databinding.ActivityLoginBinding
import com.kuteam6.homept.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}