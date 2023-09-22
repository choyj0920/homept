package com.kuteam6.homept.sns

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.kuteam6.homept.databinding.ActivitySnsCommentBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Comment
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SnsCommentActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySnsCommentBinding

    var commentAdapter: CommentAdapter? = null
    var commentList = ArrayList<Comment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySnsCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "댓글"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {

            finish()
        }
        commentAdapter = CommentAdapter(commentList, this)
        binding.snsCommentRv.adapter = commentAdapter
        binding.snsCommentRv.layoutManager = LinearLayoutManager(this@SnsCommentActivity)

        initComment()
        loadComment()

    }

    private fun loadComment() {
        // 기존 댓글 가져오기
        val pid = intent.getIntExtra("pid", 0)
        lifecycleScope.launch(Dispatchers.Main){
            val comments = ApiManager.getComment(pid)
            if(comments != null){
                commentList.clear()
                commentList.addAll(comments)

                commentAdapter?.notifyDataSetChanged()
            }else{
                //로드 실패
            }
        }

    }

    private fun initComment() {
        val db = FirebaseFirestore.getInstance()
        //댓글 작성
        binding.btnCreateComment.setOnClickListener {
            var uid = UserData.userdata?.uid.toString().toInt()
            var pid = intent.getIntExtra("pid", 0)
            Log.d("pid", pid.toString())
            var content = binding.commentWriteEt.text.toString()

            lifecycleScope.launch(Dispatchers.Main) {
                //result => cid
                val cid = ApiManager.createComment(uid, pid, content)
                Log.d("cid", cid.toString())
                if (cid != null) {
                    val currentDateTimeString =
                        SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date())

                    val newComment = Comment(
                        uid = UserData.userdata?.uid!!,
                        pid = intent.getIntExtra("pid", 0),
                        name = UserData.userdata?.name!!,
                        isTrainee = UserData.userdata?.isTrainee!!,
                        content = content,
                        create_at = currentDateTimeString
                    )
                    commentList.add(newComment)

                    commentAdapter!!.notifyDataSetChanged()


                    //입력창 초기화
                    binding.commentWriteEt.text.clear()
                    // 댓글삭제
                    commentAdapter!!.setOnItemClickListener(object :
                        CommentAdapter.CommentListener {
                        override fun onDeleteClicked(comment: Comment) {
                            AlertDialog.Builder(this@SnsCommentActivity)
                                .setTitle("댓글 삭제")
                                .setMessage("댓글을 삭제하시겠습니까?")
                                .setPositiveButton("예"){_,_->
                                    lifecycleScope.launch(Dispatchers.Main) {
                                        val result = ApiManager.deleteComment(uid, cid)
                                        Toast.makeText(
                                            this@SnsCommentActivity,
                                            if (result) "삭제 완료" else "삭제 실패",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        commentAdapter?.notifyDataSetChanged()
                                    }
                                }
                                .setNegativeButton("아니요",null)
                                .show()
                        }
                    })
                }
            }
        }
    }

}



