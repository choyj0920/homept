package com.kuteam6.homept.trainerRecommend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.FragmentRecommendVpBinding
import com.kuteam6.homept.databinding.ItemSearchBinding
import com.kuteam6.homept.restservice.data.TrainerProfile

class RecommendVPAdapter(private val itemList : ArrayList<TrainerProfile>) : RecyclerView.Adapter<RecommendVPAdapter.ViewHolder>() {
    lateinit var binding: FragmentRecommendVpBinding

    inner class ViewHolder(val binding : FragmentRecommendVpBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trainerProfile: TrainerProfile) {
            binding.recommendTvName.text = trainerProfile.name
            binding.recommendTvCategory.text = trainerProfile.usercategory
            binding.recommendTvLesson.text = trainerProfile.lesson
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = FragmentRecommendVpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

}