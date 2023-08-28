package com.kuteam6.homept.tainerProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.ItemSearchBinding
import com.kuteam6.homept.databinding.ItemTrainerReviewBinding
import com.kuteam6.homept.restservice.data.Review
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.trainerSearch.SearchAdapter

class ReviewAdapter(private val itemList : ArrayList<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemTrainerReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.tvReview.text = review.content
            binding.tvReviewer.text = review.name
            binding.rbScore.rating = review.score.toFloat()
            binding.tvCreatedAt.text = review.create_at
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        val binding = ItemTrainerReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}