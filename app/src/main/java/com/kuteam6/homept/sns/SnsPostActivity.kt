package com.kuteam6.homept.sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivitySnsCreatePostBinding
import com.kuteam6.homept.databinding.ActivitySnsPostBinding

class SnsPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnsPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("postActivity", intent.getStringExtra("name")!!)

        binding = ActivitySnsPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = intent.getStringExtra("name")
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            val snsIntent = Intent(this, HomeActivity::class.java)
            snsIntent.putExtra("fragment", "sns")
            startActivity(snsIntent)
        }

        binding.snsPostCategoryTv.text = intent.getStringExtra("category")
        binding.snsPostTitleTv.text = intent.getStringExtra("title")
        binding.snsPostContentTv.text = intent.getStringExtra("content")
        binding.snsPostCreatedAtTv.text = intent.getStringExtra("time")

    }
}