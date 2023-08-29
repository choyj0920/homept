package com.kuteam6.homept.sns

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ItemSnsBinding
import com.kuteam6.homept.restservice.data.Postdata

class SnsAdapter(private val itemList : ArrayList<Postdata>, private val context: Context) : RecyclerView.Adapter<SnsAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(postdata: Postdata)
        fun onUserItemClick(postdata: Postdata)
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemSnsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(postdata: Postdata) {
            // binding.snsProfileIv.setImageResource()
            binding.snsNameTv.text = postdata.name
            binding.snsTitleTv.text = postdata.title
            binding.snsContentTv.text = postdata.content
            binding.snsTypeTv.text = postdata.postcategory
            binding.snsUploadDateTv.text = postdata.create_at

            if (postdata.isTrainee) {
                binding.snsNameTv.setTextColor(ContextCompat.getColor(context, R.color.orange))
            } else {
                binding.snsNameTv.setTextColor(ContextCompat.getColor(context, R.color.green))
            }

            binding.itemSnsCl.setOnClickListener {
                itemClickListener.onItemClick(postdata)
            }
            binding.snsProfileIv.setOnClickListener {
                itemClickListener.onUserItemClick(postdata)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = ItemSnsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

}