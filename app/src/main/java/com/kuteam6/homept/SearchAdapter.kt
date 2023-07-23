package com.kuteam6.homept

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.ItemSearchBinding
import com.kuteam6.homept.restservice.data.TrainerProfile

class SearchAdapter(private val itemList : ArrayList<TrainerProfile>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trainerProfile: TrainerProfile) {
            binding.searchNameTv.text = trainerProfile.name
            binding.searchGenderTv.text = trainerProfile.gender
            binding.searchLocationTv.text = trainerProfile.location
            binding.searchTypeTv.text = trainerProfile.usercategory
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }


}