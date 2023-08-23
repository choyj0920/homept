package com.kuteam6.homept.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.databinding.ActivityChatListBinding
import com.kuteam6.homept.tainerProfile.CareerAdapter
import com.kuteam6.homept.tainerProfile.CareerData
import com.kuteam6.homept.tainerProfile.CertificateAdapter

class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding
   // private lateinit var chatListAdapter: ChatListAdapter

    val chatListDatas = arrayListOf<ChatListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()



    }

    private fun initRecyclerView(){
        val chatListAdapter = ChatListAdapter(chatListDatas)
        binding.rvChatList.adapter = chatListAdapter
        binding.rvChatList.layoutManager = LinearLayoutManager(this)
    }
}