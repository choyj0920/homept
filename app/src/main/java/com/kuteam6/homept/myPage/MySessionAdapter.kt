package com.kuteam6.homept.myPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.ItemMemberListBinding
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.UserData
import com.kuteam6.homept.sns.SnsAdapter
import com.kuteam6.homept.trainerSearch.SearchAdapter
import java.security.KeyStore.TrustedCertificateEntry




class MySessionAdapter(private val itemList : ArrayList<MySession>) : RecyclerView.Adapter<MySessionAdapter.ViewHolder>() {
    private lateinit var itemClickListener : SessionApprovalListener

    interface SessionApprovalListener {
        fun onApproveSession(trainerUid: Int, sessionId: Int)
        fun onDisapproveSession(trainerUid: Int, sessionId: Int)
        fun onPostReview(name: String, trainerUid: Int)
    }

    fun setOnItemClickListener(sessionApprovalListenerr : SessionApprovalListener) {
        itemClickListener = sessionApprovalListenerr
    }

    inner class ViewHolder(val binding: ItemMemberListBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(mySession: MySession){
            //트레이니가 트레이너를 조회할 때
            if(UserData.userdata?.isTrainee == true){
                //트레이니일 때 리뷰 버튼 보이게!
                binding.btnWriteReview.visibility = View.VISIBLE
                //다른 버튼 가리기
                binding.btnApprovesession.visibility = View.GONE
                binding.btnDisapprovesession.visibility = View.GONE

                binding.mypageMemberNameTv.text = mySession.myTrainerProfile?.name

                if(mySession.myTrainerProfile?.gender == "m"){
                    binding.mypageMemberGenderTv.text = "남성"
                }
                else{
                    binding.mypageMemberGenderTv.text = "여성"
                }

                binding.btnWriteReview.setOnClickListener {
                    itemClickListener.onPostReview(mySession.myTrainerProfile!!.name, mySession.myTrainerProfile!!.uid)
                }
            }
            //트레이너가 트레이니를 조회할 때
            else{
                binding.btnWriteReview.visibility = View.GONE

                binding.mypageMemberNameTv.text = mySession.myTraineeProfile?.name

                if(mySession.myTraineeProfile?.gender == "m"){
                    binding.mypageMemberGenderTv.text = "남성"
                }
                else{
                    binding.mypageMemberGenderTv.text = "여성"
                }

                //승인한 상태
                if(mySession.sessionNow == 1){
                    binding.btnApprovesession.visibility = View.GONE
                    binding.btnDisapprovesession.visibility = View.GONE
                }
                //승인 대기중
                else{
                    binding.btnApprovesession.visibility = View.VISIBLE
                    binding.btnDisapprovesession.visibility = View.VISIBLE
                }

                //승인하기
                binding.btnApprovesession.setOnClickListener {
                    var sid = mySession.sid
                    var trainerUid = UserData.userdata?.uid.toString().toInt()

                    itemClickListener.onApproveSession(trainerUid,sid)
                }
                //거절하기
                binding.btnDisapprovesession.setOnClickListener {
                    var sid = mySession.sid
                    var trainerUid = UserData.userdata?.uid.toString().toInt()

                    itemClickListener.onDisapproveSession(trainerUid,sid)
                }

                val userCategory = mySession.myTraineeProfile?.usercategory

                if(userCategory != null){
                    val categoryNames = listOf("체형 교정","근력, 체력 강화", "유아 체육", "재활", "시니어 건강", "다이어트")
                    var resultCategoryList: String = ""

                    for(i in userCategory.indices){
                        if(userCategory[i] == '1'){
                            resultCategoryList += "${ categoryNames[i]}/"
                        }
                    }

                    binding.mypageMemberCategoryTv.text = resultCategoryList.trim()
                }
            }



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
        holder.bind(itemList[position])
    }
}
