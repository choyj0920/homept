package com.kuteam6.homept.tainerProfile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.kuteam6.homept.Friend
import com.kuteam6.homept.R
import com.kuteam6.homept.chat.ChatModel
import com.kuteam6.homept.databinding.ActivityTrainersProfileBinding
import com.kuteam6.homept.myPage.ReviewPostActivity
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

//TODO:리뷰 사진 리사이클러뷰
//TODO: 이력 리사이클러뷰 사이즈 조정해서 스크롤 안되고 다나오게

class TrainersProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainersProfileBinding
    private lateinit var careerAdapter: CareerAdapter
    private lateinit var certificateAdapter: CertificateAdapter

    val careerDatas = arrayListOf<CareerData>()
    val certificateDatas = arrayListOf<CertificateData>()
    var reviewDatas = arrayListOf<Review>()

    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private var uid: String? = null
    private lateinit var trainerUid: String

    private var chatRoomUid: String? = null
    private var destinationUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainersProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = Firebase.auth.currentUser?.uid.toString()

        binding.toolbarBackIv.toolbarBackMainTv.text = "트레이너 프로필"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        Log.d("isTrainee", UserData.userdata?.isTrainee.toString())
        if (!UserData.userdata?.isTrainee!!) {
            binding.btnPt.visibility = View.GONE
        }

        // firebase 프로필 사진 가져오기
        val photo = binding.ivImg
        val suid = intent.getIntExtra("uid", 0)
        fireDatabase.child("users").orderByChild("suid").equalTo(suid.toDouble())
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    trainerUid = snapshot.key!!
                    destinationUid = trainerUid
                    //Log.d("tuid",trainerUid.toString())
                    fireDatabase.child("users").child(trainerUid)
                        .addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                Log.d("profile error", error.toString())
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userProfile = snapshot.getValue<Friend>()
                                println(userProfile)
                                Log.d("user", userProfile.toString())
                                Log.d("imageurl", userProfile?.profileImageUrl.toString())
                                if (userProfile?.profileImageUrl == "null") {
                                    photo!!.setImageResource(R.drawable.empty_profile)
                                } else {
                                    Glide.with(this@TrainersProfileActivity)
                                        .load(userProfile?.profileImageUrl)
                                        //.apply(RequestOptions().circleCrop())
                                        .into(photo!!)
                                }
                                Log.d("imageurl2", userProfile?.profileImageUrl.toString())
                            }
                        })
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        binding.tvName.text = intent.getStringExtra("name")
        binding.tvCategory.text = intent.getStringExtra("usercategory")
        binding.tvLesson.text = intent.getStringExtra("lesson")


        val career = intent.getStringExtra("career")!!.split("\r?\n|\r".toRegex()).toTypedArray()

        for (careerString in career) {
            careerDatas.add(CareerData(careerString))
        }

//        val certificate = intent.getStringExtra("certificate")!!.split("\r?\n|\r".toRegex()).toTypedArray()
//
//        for (certificateString in certificate) {
//            certificateDatas.add(CertificateData(certificateString))
//        }


        initDataList()
        initRecyclerView()

        //PT 신청 버튼
        binding.btnPt.setOnClickListener {
            showPtApplyConfirmationDialog()

//            val applyConfirmIntent = Intent(this@TrainersProfileActivity, PtApplyConfirmActivity::class.java)
//            applyConfirmIntent.putExtra("trainerUid", intent.getIntExtra("uid", 0))
//            startActivity(applyConfirmIntent)
        }

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
        fireDatabase.child("chatrooms").orderByChild("users/$uid").equalTo(true)
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



    private fun initRecyclerView() {
        val careerAdapter = CareerAdapter(careerDatas)
        binding.rvCareer.adapter = careerAdapter
        binding.rvCareer.layoutManager = LinearLayoutManager(this)

        val certificateAdapter = CertificateAdapter(certificateDatas)
        binding.rvCertificate.adapter = certificateAdapter
        binding.rvCertificate.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
            val reviewList = ApiManager.getReview(intent.getIntExtra("uid", 0))
            if (reviewList != null) {
                reviewDatas = reviewList.toTypedArray().toCollection(ArrayList<Review>())
                val reviewAdapter = ReviewAdapter(reviewDatas)
                binding.trainerReviewRv.adapter = reviewAdapter
                binding.trainerReviewRv.layoutManager = LinearLayoutManager(applicationContext)
                reviewAdapter.setOnItemClickListener(object : ReviewAdapter.OnItemClickListener {
                    override fun onEditItemClick(review: Review) {
                        Log.d("edit", intent.getStringExtra("name").toString())
                        val reviewEditIntent =
                            Intent(this@TrainersProfileActivity, ReviewPostActivity::class.java)
                        reviewEditIntent.putExtra("name", intent.getStringExtra("name"))
                        reviewEditIntent.putExtra("trainerUid", intent.getIntExtra("uid", 0))
                        reviewEditIntent.putExtra("isCreate", false)
                        startActivity(reviewEditIntent)
                        reviewAdapter.notifyDataSetChanged()
                    }

                    override fun onDeleteItemClick(review: Review) {
                        Log.d("delete", intent.getStringExtra("name").toString())
                        val builder = AlertDialog.Builder(this@TrainersProfileActivity)
                        builder.setTitle("제목")
                            .setMessage("댓글을 삭제하시겠습니까?")
                            .setPositiveButton("확인") { dialog, id ->
                                lifecycleScope.launch(Dispatchers.Main) {
                                    ApiManager.deleteReview(
                                        intent.getIntExtra("uid", 0),
                                        review.uid
                                    )
                                    reviewDatas.remove(review)
                                    reviewAdapter.notifyDataSetChanged()
                                }
                            }
                        val dialog = builder.create()
                        dialog.show()
                    }
                })
            }
        }
    }

    private fun initDataList() {

//        with(careerDatas){
//            add(CareerData("미스터 올림피아 8회 연속 우승"))
//            add(CareerData("한국체육대학교 박사"))
//            add(CareerData("트레이닝 경력 8년"))
//        }

        with(certificateDatas) {
            add(CertificateData("생활체육지도자 1급"))
        }

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

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
            val reviewList = ApiManager.getReview(intent.getIntExtra("uid", 0))
            if (reviewList != null) {
                reviewDatas = reviewList.toTypedArray().toCollection(ArrayList<Review>())
                val reviewAdapter = ReviewAdapter(reviewDatas)
                binding.trainerReviewRv.adapter = reviewAdapter
                binding.trainerReviewRv.layoutManager = LinearLayoutManager(applicationContext)
                reviewAdapter.setOnItemClickListener(object : ReviewAdapter.OnItemClickListener {
                    override fun onEditItemClick(review: Review) {
                        Log.d("edit", intent.getStringExtra("name").toString())
                        val reviewEditIntent =
                            Intent(this@TrainersProfileActivity, ReviewPostActivity::class.java)
                        reviewEditIntent.putExtra("name", intent.getStringExtra("name"))
                        reviewEditIntent.putExtra("trainerUid", intent.getIntExtra("uid", 0))
                        reviewEditIntent.putExtra("isCreate", false)
                        startActivity(reviewEditIntent)
                        reviewAdapter.notifyDataSetChanged()
                    }

                    override fun onDeleteItemClick(review: Review) {
                        Log.d("delete", intent.getStringExtra("name").toString())
                        val builder = AlertDialog.Builder(this@TrainersProfileActivity)
                        builder.setTitle("제목")
                            .setMessage("댓글을 삭제하시겠습니까?")
                            .setPositiveButton("확인") { dialog, id ->
                                lifecycleScope.launch(Dispatchers.Main) {
                                    ApiManager.deleteReview(
                                        intent.getIntExtra("uid", 0),
                                        review.uid
                                    )
                                    reviewDatas.remove(review)
                                    reviewAdapter.notifyDataSetChanged()
                                }
                            }
                        val dialog = builder.create()
                        dialog.show()
                    }
                })
            }
        }
    }
}