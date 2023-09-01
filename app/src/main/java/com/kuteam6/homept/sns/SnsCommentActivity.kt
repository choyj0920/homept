package com.kuteam6.homept.sns

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.databinding.ActivitySnsCommentBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Comment
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SnsCommentActivity: AppCompatActivity() {
    lateinit var binding: ActivitySnsCommentBinding

    var commentAdapter: CommentAdapter? = null
    var commentList = ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySnsCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        commentAdapter = CommentAdapter(commentList)
        binding.snsCommentRv.adapter = commentAdapter

        binding.toolbarBackIv.toolbarBackMainTv.text = "댓글"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        initComment()
    }

    private fun initComment(){
        //댓글 작성
        binding.btnCreateComment.setOnClickListener {
            var uid = UserData.userdata?.uid.toString().toInt()
            var pid = 1
            var content = binding.commentWriteEt.text.toString()

            lifecycleScope.launch(Dispatchers.Main){
                var result = ApiManager.createComment(uid, pid, content)
                if (result != null){
                    commentAdapter = CommentAdapter(commentList)
                    binding.snsCommentRv.adapter = commentAdapter
                    //binding.snsCommentRv.layoutManager = LinearLayoutManager()
                }
            }
        }
    }

}

