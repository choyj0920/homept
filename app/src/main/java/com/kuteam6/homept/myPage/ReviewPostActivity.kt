package com.kuteam6.homept.myPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityMypageMemberListBinding
import com.kuteam6.homept.databinding.ActivityReviewPostBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewPostActivity : AppCompatActivity() {
    lateinit var binding: ActivityReviewPostBinding
    var score : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "리뷰 작성"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }
        binding.toolbarBackIv.toolbarBackSubTv.text = "완료"

        binding.reviewTrainerNameTv.text = intent.getStringExtra("name")

        binding.rbScore.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            score = rating.toDouble()
        }

        binding.toolbarBackIv.toolbarBackSubTv.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                ApiManager.createReview(intent.getIntExtra("trainerUid", 0), UserData.userdata!!.uid, score, binding.reviewEt.text.toString())
            }
            finish()
        }
    }
}