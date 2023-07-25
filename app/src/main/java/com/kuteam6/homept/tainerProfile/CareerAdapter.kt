package com.kuteam6.homept.tainerProfile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.SearchAdapter
import com.kuteam6.homept.databinding.ActivityTrainersProfileBinding
import com.kuteam6.homept.databinding.ItemCareerListBinding
import com.kuteam6.homept.databinding.ItemSearchBinding
import com.kuteam6.homept.restservice.data.TrainerProfile

class CareerAdapter(private val itemList: ArrayList<CareerData>) :
    RecyclerView.Adapter<CareerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCareerListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(careerData: CareerData) {
            binding.tvCareerName.text = careerData.careerName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareerAdapter.ViewHolder {
        val binding =
            ItemCareerListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: CareerAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}