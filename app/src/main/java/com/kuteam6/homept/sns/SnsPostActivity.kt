package com.kuteam6.homept.sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.View
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivitySnsCreatePostBinding
import com.kuteam6.homept.databinding.ActivitySnsPostBinding
import com.kuteam6.homept.restservice.data.UserData

class SnsPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnsPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("postActivity", intent.getStringExtra("name")!!)

        binding = ActivitySnsPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = intent.getStringExtra("name")
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        binding.snsPostCategoryTv.text = intent.getStringExtra("category")
        binding.snsPostTitleTv.text = intent.getStringExtra("title")
        binding.snsPostContentTv.text = intent.getStringExtra("content")
        binding.snsPostCreatedAtTv.text = intent.getStringExtra("time")

        if (intent.getIntExtra("uid", 0) != UserData.userdata?.uid) {
            binding.snsEditBtn.visibility = View.GONE
        }

        binding.snsEditBtn.setOnClickListener {
            val editIntent = Intent(this, SnsCreatePostActivity::class.java)
            editIntent.putExtra("isCreate", false)
            editIntent.putExtra("category", intent.getStringExtra("category"))
            editIntent.putExtra("title", intent.getStringExtra("title"))
            editIntent.putExtra("content", intent.getStringExtra("content"))
            editIntent.putExtra("uid", intent.getIntExtra("uid", 0))
            editIntent.putExtra("pid", intent.getIntExtra("pid", 0))
            startActivity(editIntent)
        }


    }
}