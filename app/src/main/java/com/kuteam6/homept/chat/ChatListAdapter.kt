package com.kuteam6.homept.chat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.ItemCareerListBinding
import com.kuteam6.homept.databinding.ItemChatBinding
import com.kuteam6.homept.tainerProfile.CareerData


class ChatListAdapter(private val itemList: ArrayList<ChatListItem>) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatListItem: ChatListItem) {
            //binding.tvCareerName.text = careerData.careerName
            binding.civImg.setImageResource(chatListItem.img)
            binding.tvName.text = chatListItem.name
            binding.tvLastChat.text = chatListItem.lastChat

            //채팅창 선택 시 이동
            itemView.setOnClickListener {
                val intent = Intent(binding.root.context,ChatRoomActivity::class.java)
                //TODO putExtra uid 등

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder {
        val binding =
            ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ChatListAdapter.ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }
}