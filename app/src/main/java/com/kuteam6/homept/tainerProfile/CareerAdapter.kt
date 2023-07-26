package com.kuteam6.homept.tainerProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.ItemCareerListBinding

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