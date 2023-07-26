package com.kuteam6.homept.trainerSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.ItemSearchBinding
import com.kuteam6.homept.restservice.data.TrainerProfile

class SearchAdapter(private val itemList : ArrayList<TrainerProfile>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(trainerProfile : TrainerProfile)
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trainerProfile: TrainerProfile) {
            binding.searchNameTv.text = trainerProfile.name
            binding.searchGenderTv.text = trainerProfile.gender
            binding.searchLocationTv.text = trainerProfile.location
            binding.searchTypeTv.text = trainerProfile.usercategory
            binding.itemSearchCl.setOnClickListener {
                itemClickListener.onItemClick(trainerProfile)
            }
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