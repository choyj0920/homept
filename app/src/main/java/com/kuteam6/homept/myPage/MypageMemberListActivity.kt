package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.databinding.ActivityMypageMemberListBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MypageMemberListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMypageMemberListBinding

    var mySessionAdapter: MySessionAdapter? = null
    var mySessionList = ArrayList<MySession>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMypageMemberListBinding.inflate(layoutInflater)

        binding.cbGetSession.visibility = View.GONE

        //트레이니
        if(UserData.userdata?.isTrainee == true){
            binding.cbGetSession.isChecked = false
            //binding.etGetSessionMyuid.hint = UserData.userdata?.name
            binding.tvGetSessionResult.text = "내 트레이너"
        }
        //트레이너
        else{
            binding.cbGetSession.isChecked = true
            binding.tvGetSessionResult.text = "내 트레이니"
        }

        binding.btnGetSession.setOnClickListener {
            var uid = UserData.userdata?.uid.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main){
                var resultList = ApiManager.getMySession(binding.cbGetSession.isChecked, uid)
                if(resultList != null){
                    //binding.tvGetSessionResult.text = resultList.toString()
                    mySessionList = resultList.toTypedArray().toCollection(ArrayList<MySession>())
                    mySessionAdapter = MySessionAdapter(mySessionList)
                    binding.rvSession.adapter = mySessionAdapter
                    binding.rvSession.layoutManager = LinearLayoutManager(this@MypageMemberListActivity)
                    mySessionAdapter!!.setOnItemClickListener(object : MySessionAdapter.SessionApprovalListener{
                        override fun onApproveSession(trainerUid: Int, sessionId: Int) {
                            AlertDialog.Builder(this@MypageMemberListActivity)
                                .setTitle("승인 확인")
                                .setMessage("정말로 승인하시겠습니까?")
                                .setPositiveButton("예"){_, _->
                                    lifecycleScope.launch(Dispatchers.Main){
                                        val result = ApiManager.approveSession(trainerUid, sessionId)
                                        Toast.makeText(this@MypageMemberListActivity, if(result) "승인 완료" else "승인 실패",Toast.LENGTH_SHORT).show()
                                        mySessionAdapter?.notifyDataSetChanged()
                                    }
                                }
                                .setNegativeButton("아니요", null)
                                .show()
                        }

                        override fun onDisapproveSession(trainerUid: Int, sessionId: Int) {
                            AlertDialog.Builder(this@MypageMemberListActivity)
                                .setTitle("거절 확인")
                                .setMessage("정말로 거절하시겠습니까?")
                                .setPositiveButton("예"){_,_->
                                    lifecycleScope.launch(Dispatchers.Main){
                                        val result = ApiManager.approveSession(trainerUid,sessionId,true)
                                        Toast.makeText(this@MypageMemberListActivity, if(result) "거절 완료" else "거절 실패", Toast.LENGTH_SHORT).show()
                                        mySessionAdapter?.notifyDataSetChanged()
                                    }
                                }
                                .setNegativeButton("아니요", null)
                                .show()
                        }

                        override fun onPostReview(name: String, trainerUid: Int) {
                            val postReviewIntent = Intent(this@MypageMemberListActivity, ReviewPostActivity::class.java)
                            postReviewIntent.putExtra("name", name)
                            postReviewIntent.putExtra("trainerUid", trainerUid)
                            startActivity(postReviewIntent)
                        }

                    })
                }
            }
        }
        setContentView(binding.root)

        binding.toolbarMypageMemberList.toolbarBackMainTv.text = "내 리스트"
        binding.toolbarMypageMemberList.toolbarBackIv.setOnClickListener {
            finish()
        }

    }
}

