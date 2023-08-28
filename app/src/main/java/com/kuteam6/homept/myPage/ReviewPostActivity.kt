package com.kuteam6.homept.myPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityPtApproveBinding
import com.kuteam6.homept.databinding.ActivityReviewPostBinding

class ReviewPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityReviewPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_post)

        binding.toolbarBackIv.toolbarBackMainTv.text = "리뷰 작성"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }
    }
}