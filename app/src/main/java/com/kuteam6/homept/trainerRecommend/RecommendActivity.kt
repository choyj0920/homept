package com.kuteam6.homept.trainerRecommend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.kuteam6.homept.FBRef
import com.kuteam6.homept.R
import com.kuteam6.homept.chat.ChatModel
import com.kuteam6.homept.databinding.ActivityRecommendBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class RecommendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendBinding
    var recommendList = ArrayList<TrainerProfile>()
    var recommendVPAdapter: RecommendVPAdapter? = null

    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private var uid: String? = null

    private var chatRoomUid: String? = null
    private var destinationUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        binding.toolbarBackIv.toolbarBackMainTv.text = "트레이너 추천"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        val category = intent.getStringExtra("category")
        val gender = intent.getStringExtra("gender")
        val location = intent.getStringExtra("location")

        // 추천 api 호출
        lifecycleScope.launch(Dispatchers.Main) {
            val resultList =
                ApiManager.searchTrainer(category = category!!, gender = gender, location = location!!)
            if(resultList!=null) {
                recommendList = resultList.toTypedArray().toCollection(ArrayList<TrainerProfile>())

                for(list in recommendList) {
                    if(list.gender == "f")
                        list.gender = "여자"
                    else if(list.gender == "m")
                        list.gender = "남자"

                    var category : String = ""

                    if(list.usercategory.get(0) == '1')
                        category += "체형교정, "
                    if(list.usercategory.get(1) == '1')
                        category += "근력,체력강화, "
                    if(list.usercategory.get(2) == '1')
                        category += "유아체육, "
                    if(list.usercategory.get(3) == '1')
                        category += "재활, "
                    if(list.usercategory.get(4) == '1')
                        category += "시니어건강, "
                    if(list.usercategory.get(5) == '1')
                        category += "다이어트, "

                    category = category.trim().substring(0, category.length-2)
                    list.usercategory = category
                }
            }
            recommendVPAdapter = RecommendVPAdapter(recommendList, applicationContext, lifecycleScope)
            binding.recommendVp.adapter = recommendVPAdapter
            binding.recommendVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.recommendTvViewpager.text = getString(R.string.recommend_viewpager_text, 1, recommendList.size)
            recommendVPAdapter!!.setOnItemClickListener(object : RecommendVPAdapter.OnItemClickListener {
                override fun onItemClick(trainerProfile: TrainerProfile) {
//                    val applyConfirmIntent = Intent(this@RecommendActivity, PtApplyConfirmActivity::class.java)
//                    applyConfirmIntent.putExtra("trainerUid", trainerProfile.uid)
//                    startActivity(applyConfirmIntent)
//                      카톡으로 말씀드린 부분입니다ㅎㅎ 제가 확신이 없어서 수정을 안했습니다 죄송합니다

                    showPtApplyConfirmationDialog()
                }
            })
        }
        binding.recommendVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.recommendTvViewpager.text = getString(R.string.recommend_viewpager_text, position+1, recommendList.size)
            }
        })
    }

    private fun showPtApplyConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("PT 신청")
        builder.setMessage("PT를 신청하시겠습니까?")

        //"예"버튼
        builder.setPositiveButton("예") { _, _ ->
            sendPTmessage()
            initapplysession()
        }

        //"아니요" 버튼
        builder.setNegativeButton("아니요") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initapplysession() {
        val traineeuid = UserData.userdata?.uid.toString().toInt()
        Log.d("traineeuid", traineeuid.toString())

        val traineruid = intent.getIntExtra("uid", 0)
        Log.d("traineruid", traineruid.toString())

        lifecycleScope.launch(Dispatchers.Main) {
            Log.d("sid", MySession.mysession?.sid.toString())
            val result = ApiManager.applySession(traineeuid, traineruid)
            val message = if (result) "신청 완료" else "신청 실패"
            showToastMessage(message)
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun sendPTmessage() {

        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
        val curTime = dateFormat.format(Date(time)).toString()

        val chatModel = ChatModel()
        chatModel.users.put(uid.toString(), true)
        chatModel.users.put(destinationUid!!, true)

        val name = UserData.userdata!!.name

        val message = "${name}님이 PT를 신청하셨습니다!\nPT신청서를 작성하고 상담해주세요!"

        val comment = ChatModel.Comment(uid, message, curTime, true)

        if (chatRoomUid == null) {
            fireDatabase.child("chatrooms").push().setValue(chatModel).addOnSuccessListener {
                //채팅방 생성
                checkChatRoom()
                //메세지 보내기
                Handler().postDelayed({
                    println(chatRoomUid)
                    fireDatabase.child("chatrooms").child(chatRoomUid.toString())
                        .child("comments").push().setValue(comment)
                }, 1000L)
                Log.d("chatUidNull dest", "$destinationUid")
            }
        } else {
            fireDatabase.child("chatrooms").child(chatRoomUid.toString()).child("comments")
                .push().setValue(comment)
            Log.d("chatUidNotNull dest", "$destinationUid")
        }
        checkChatRoom()
    }


    private fun checkChatRoom() {
        fireDatabase.child("chatrooms").orderByChild("users/${FBRef.uid}").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children) {
                        println(item)
                        val chatModel = item.getValue<ChatModel>()
                        if (chatModel?.users!!.containsKey(destinationUid)) {
                            chatRoomUid = item.key
                        }
                    }
                }
            })
    }
}