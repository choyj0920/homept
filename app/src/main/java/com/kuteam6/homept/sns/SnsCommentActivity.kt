package com.kuteam6.homept.sns

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivitySnsCommentBinding

class SnsCommentActivity: AppCompatActivity() {
    lateinit var binding: ActivitySnsCommentBinding

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        binding = ActivitySnsCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "댓글"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }
    }
}