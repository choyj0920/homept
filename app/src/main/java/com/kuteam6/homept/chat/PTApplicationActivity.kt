package com.kuteam6.homept.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.kuteam6.homept.FBRef
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityPtapplicationBinding

class PTApplicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPtapplicationBinding

    //private lateinit var uid:String
    //private lateinit var destinationUid:String
    private lateinit var chatRoomUid: String
    private val db = FirebaseDatabase.getInstance().reference
    //private val applicationModel = ApplicationModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPtapplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "PT 신청서"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //uid = intent.getStringExtra("uid").toString()
        //destinationUid = intent.getStringExtra("destinationUid").toString()
        chatRoomUid = intent.getStringExtra("chatRoomUid").toString()

        val purpose = binding.etPurpose
        val age = binding.etAge
        val time = binding.etTime
        val start = binding.etStart
        val place = binding.etPlace
        val etc = binding.etEtc

        db.child(FBRef.chatrooms.toString()).child(chatRoomUid).child("application")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val datas = snapshot.getValue<Application>()
                    if(datas != null){
                        purpose.setText(datas.purpose)
                        age.setText(datas.age)
                        time.setText(datas.time)
                        start.setText(datas.start)
                        place.setText(datas.place)
                        etc.setText(datas.etc)
                    }else{

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("error",error.toString())
                }
            })



        binding.btnSubmit.setOnClickListener {
            submit()

        }

    }

    private fun submit() {
        val purpose = binding.etPurpose.text
        val age = binding.etAge.text
        val time = binding.etTime.text
        val start = binding.etStart.text
        val place = binding.etPlace.text
        val etc = binding.etEtc.text

        db.child(FBRef.chatrooms.toString()).child(chatRoomUid).child("application").setValue(
            Application(
                purpose.toString(),
                age.toString(),
                time.toString(),
                start.toString(),
                place.toString(),
                etc.toString()
            )
        ).addOnSuccessListener {
            Toast.makeText(this,"신청서를 저장했습니다",Toast.LENGTH_LONG)
            onBackPressed()
        }.addOnFailureListener {
            Toast.makeText(this,"오류로 인해 신청서를 저장하지 못했습니다.",Toast.LENGTH_LONG)

        }
    }


}