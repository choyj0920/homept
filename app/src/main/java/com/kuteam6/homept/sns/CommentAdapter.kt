package com.kuteam6.homept.sns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ItemCommentBinding
import com.kuteam6.homept.restservice.data.Comment
import com.kuteam6.homept.restservice.data.UserData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class CommentAdapter(private val commentList: ArrayList<Comment>, private val listener: SnsCommentActivity):RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
   private lateinit var itemClickListener : CommentListener

   interface CommentListener{
        fun onDeleteClicked(comment: Comment)
   }

    fun setOnItemClickListener(commentListener: CommentListener){
        itemClickListener = commentListener
    }

    inner class ViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(comment: Comment){
            binding.commentTextTv.text = comment.content
            binding.commentAuthorTv.text = comment.name
            binding.commentCreateAtTv.text = comment.create_at

            if(comment.uid == UserData.userdata?.uid){
                binding.moreInfoCommentIv.setOnClickListener {view ->
                    val bottomSheetDialog = BottomSheetDialog(view.context)
                    val sheetView = LayoutInflater.from(view.context).inflate(R.layout.dialog_bottom_comment_sheet, null)

                    val deleteButton: Button = sheetView.findViewById(R.id.btn_comment_delete)
                    deleteButton.setOnClickListener {
                        itemClickListener.onDeleteClicked(comment)
                        bottomSheetDialog.dismiss()
                    }
                    val cancelButton: Button = sheetView.findViewById(R.id.btn_cancel_in_comment)
                    cancelButton.setOnClickListener {
                        bottomSheetDialog.dismiss()
                    }

                    bottomSheetDialog.setContentView(sheetView)
                    bottomSheetDialog.show()
                }
            }

//            //삭제하는 기능이 있는 부분
//            if(comment.uid == UserData.userdata?.uid){
//
//                binding.moreInfoCommentIv.visibility = View.VISIBLE
//
//                binding.moreInfoCommentIv.setOnClickListener {view ->
//                    AlertDialog.Builder(view.context)
//                        .setTitle("댓글 삭제")
//                        .setMessage("댓글을 삭제하시겠습니까?")
//                        .setPositiveButton("예"){
//                                dialog, which ->
//                            listener.onDeleteClicked(commentList[position])
//                        }
//                        .setNegativeButton("아니오",null)
//                        .show()
//
//                }
//            }
//            else{
//                binding.moreInfoCommentIv.visibility = View.GONE
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(commentList[position])
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

//    //댓글 시간 계산
//    fun getRelativeTime(time: String): String{
//        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//        val past = sdf.parse(time)
//        val now = Date()
//
//        if(past.after(now)) return "방금"
//
//        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
//        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
//        val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
//        val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
//
//        return when{
//            minutes < 1 -> "$seconds 초 전"
//            minutes < 60 -> "$minutes 분 전"
//            hours < 24 -> "$hours 시간 전"
//            days < 30 -> "$days 일 전"
//            else -> SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(past)
//        }
//    }

}