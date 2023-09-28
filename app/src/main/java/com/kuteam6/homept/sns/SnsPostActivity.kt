package com.kuteam6.homept.sns

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivitySnsCreatePostBinding
import com.kuteam6.homept.databinding.ActivitySnsPostBinding
import com.kuteam6.homept.loginSignup.TestActivity
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.util.concurrent.Executors
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class SnsPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnsPostBinding
    private val database = FirebaseDatabase.getInstance()
    private lateinit var likesRef: DatabaseReference

    companion object{
        private const val TAG = "SnsPostActivity"
    }
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
            binding.snsDeleteBtn.visibility = View.GONE
        }

        lifecycleScope.launch(Dispatchers.Main) {
            if (intent.getIntExtra("isImagehave", 0) == 1) {
                Log.d("pid", intent.getIntExtra("pid", 0).toString())
                Glide.with(applicationContext)
                    .load(ApiManager.getSnsImage(intent.getIntExtra("pid", 0)))
                    .into(binding.imageView)
            } else {
                binding.imageView.visibility = View.GONE
            }
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
            finish()
        }

        binding.snsDeleteBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                ApiManager.deletePost(UserData.userdata!!.uid, intent.getIntExtra("pid", 0))
                finish()
            }
        }

        val postId = intent.getIntExtra("pid",0).toString()
        likesRef = database.getReference("likes").child(postId)

        likesRef.child(UserData.userdata?.uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.snsLikeIv.isSelected = snapshot.exists()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value", error.toException())
            }
        })

        //좋아요 버튼
        binding.snsLikeIv.setOnClickListener {
            val likeButton = it as ImageView
            if(likeButton.isSelected){
                //좋아요 해제
                likeButton.isSelected = false
                showLikeSnackBar("좋아요 취소", false)

                likesRef.child(UserData.userdata?.uid.toString()).removeValue()
            }
            else{
                likeButton.isSelected = true
                showLikeSnackBar("좋아요를 눌렀어요!", true)

                likesRef.child(UserData.userdata?.uid.toString()).setValue(true)
            }
        }

        //댓글 창으로 이동하는 부분
        binding.snsCommentIv.setOnClickListener {
            val commentIntent = Intent(this, SnsCommentActivity::class.java)
            commentIntent.putExtra("pid", intent.getIntExtra("pid", 0))
            startActivity(commentIntent)
        }
    }

    private fun showLikeSnackBar(message: String, like: Boolean){
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        if(like){
            snackBar.setAction("❤️"){}
        }
        else{

        }
        snackBar.show()
    }

}