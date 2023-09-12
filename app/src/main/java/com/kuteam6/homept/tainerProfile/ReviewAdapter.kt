package com.kuteam6.homept.tainerProfile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.chat.User
import com.kuteam6.homept.databinding.ItemSearchBinding
import com.kuteam6.homept.databinding.ItemTrainerReviewBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Postdata
import com.kuteam6.homept.restservice.data.Review
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.restservice.data.UserData
import com.kuteam6.homept.trainerSearch.SearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewAdapter(private val itemList : ArrayList<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onEditItemClick(review: Review)
        fun onDeleteItemClick(review: Review)
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemTrainerReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.tvReview.text = review.content
            binding.tvReviewer.text = review.name
            binding.rbScore.rating = review.score.toFloat()
            binding.tvCreatedAt.text = review.create_at

            if (review.uid != UserData.userdata!!.uid) {
                binding.reviewDeleteBtn.visibility = View.GONE
                binding.reviewEditBtn.visibility = View.GONE
            }

            binding.reviewDeleteBtn.setOnClickListener {
                itemClickListener.onDeleteItemClick(review)
            }

            binding.reviewEditBtn.setOnClickListener {
                itemClickListener.onEditItemClick(review)
            }
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