package com.kuteam6.homept.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuteam6.homept.databinding.ActivityChatListBinding
import com.kuteam6.homept.restservice.data.UserData


class ChatListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatListBinding
   // private lateinit var chatListAdapter: ChatListAdapter
    lateinit var adapter: ChatListAdapter

    private lateinit var mDbRef : DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    private lateinit var userList : ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 초기화 Firebase.auth
        mAuth = FirebaseAuth.getInstance()

        // Firebase db 초기화
        mDbRef = Firebase.database.reference

        //리스트 초기화
        initRecyclerView()


        // 사용자 정보 가져오기
        mDbRef.child("user").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    // 유저 정보
                    val currentUser = postSnapshot.getValue(User::class.java)

                    //mAuth.currentUser?.uid
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initRecyclerView(){
        val chatListAdapter = ChatListAdapter(userList)
        binding.rvChatList.adapter = chatListAdapter
        binding.rvChatList.layoutManager = LinearLayoutManager(this)
    }
}