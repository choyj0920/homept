package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.databinding.ActivityMypageMemberListBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.MySession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MypageMemberListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMypageMemberListBinding

    var sessionAdapter: MySessionAdapter? = null
    var sessionList = ArrayList<MySession>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMypageMemberListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMypageMemberList.toolbarBackMainTv.text = "담당 리스트"
        binding.toolbarMypageMemberList.toolbarBackIv.setOnClickListener {
            val memberListIntent = Intent(this, MypageFragment::class.java)
            startActivity(memberListIntent)
        }

    }
}