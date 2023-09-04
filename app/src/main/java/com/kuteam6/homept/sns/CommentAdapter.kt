package com.kuteam6.homept.sns

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.ItemCommentBinding
import com.kuteam6.homept.restservice.data.Comment

class CommentAdapter(private val commentList: ArrayList<Comment>):RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(comment: Comment){
            binding.commentTextTv.text = comment.content
            binding.commentAuthorTv.text = comment.name
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


}