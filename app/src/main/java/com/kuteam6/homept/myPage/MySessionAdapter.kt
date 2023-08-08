package com.kuteam6.homept.myPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.ItemMemberListBinding
import com.kuteam6.homept.restservice.data.MySession

class MySessionAdapter(private val itemList : ArrayList<MySession>) : RecyclerView.Adapter<MySessionAdapter.ViewHolder>() {



    class ViewHolder(val binding: ItemMemberListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(mySession: MySession){
            binding.mypageMemberTv.text = mySession.sid.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMemberListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}
