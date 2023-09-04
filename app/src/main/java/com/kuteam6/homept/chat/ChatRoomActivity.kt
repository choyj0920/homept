package com.kuteam6.homept.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kuteam6.homept.databinding.ActivityChatRoomBinding
import com.kuteam6.homept.restservice.data.UserData

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var receiverName: String
    private lateinit var receiverUid: String

    private lateinit var binding: ActivityChatRoomBinding

    lateinit var mAuth : FirebaseAuth
    lateinit var mDbRef: DatabaseReference

    private lateinit var receiverRoom: String // 받는 대화방
    private lateinit var senderRoom: String // 보낸 대화방

    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val messageAdapter:MessageAdapter = MessageAdapter(this,messageList)

        binding.rvMessage.layoutManager = LinearLayoutManager(this)
        binding.rvMessage.adapter = messageAdapter

        // 넘어온 데이터 변수에 담기
        receiverName = intent.getStringExtra("name").toString()
        receiverUid = intent.getStringExtra("uid").toString()

        // 액션바에 상대방 이름 보여주기
        supportActionBar?.title = receiverName

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        //접속자 uid mAuth.currentUser?.uid
        val senderUid = mAuth.currentUser?.uid

        //보낸이 방
        senderRoom = receiverUid + senderUid

        // 받는이 방
        receiverRoom = senderUid + receiverUid

        supportActionBar?.title = receiverName

        binding.ivSendingArrow.setOnClickListener {

            val message = binding.etMessage.text.toString()
            val messageObject = Message(message, senderUid)

            //데이터 저장
            mDbRef.child("chats").child(senderRoom).child("message").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(receiverRoom).child("messages").push().setValue(messageObject)
                }
            binding.etMessage.setText("")
        }

        // 메시지 가져오기
        mDbRef.child("chats").child(senderRoom).child("messages").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()

                for(postSnapshot in snapshot.children){
                    val message = postSnapshot.getValue(Message::class.java)
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}