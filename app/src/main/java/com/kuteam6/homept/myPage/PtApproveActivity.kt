package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityPtApproveBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PtApproveActivity: AppCompatActivity() {
    lateinit var binding: ActivityPtApproveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPtApproveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ptApproveToolbarBackIv.toolbarBackMainTv.text = "pt 승인"
        binding.ptApproveToolbarBackIv.toolbarBackIv.setOnClickListener {
            val backIntent = Intent(this, MypageMemberListActivity::class.java)
            startActivity(backIntent)
        }

        // 성별
        val gender = UserData.userdata?.gender.toString()

        if(gender.equals('m')){
            binding.etGender.setText("Male")
        }
        else{
            binding.etGender.setText("Female")
        }

        // 카테고리
        val ptapproveCategory = UserData.userdata?.userCategory.toString()

        val categories = StringBuilder()

        for(i in ptapproveCategory.indices){
            if(ptapproveCategory[i] == '1'){
                when(i){
                    0 -> categories.append("체형 교정")
                    1 -> categories.append("근력/ 체력 강화")
                    2 -> categories.append("유아 체육")
                    3 -> categories.append("재활")
                    4 -> categories.append("시니어 건강")
                    5 -> categories.append("다이어트")
                }

                if(i != ptapproveCategory.length - 1){
                    categories.append(", ")
                }
            }
        }
        binding.etCategory.setText(categories.toString())

        // 지역
        val location = UserData.userdata?.location.toString()
        binding.etLocation.setText(location)


        //트레이너에 대한 처리
        //매칭 승인
        binding.btnApprovesession.setOnClickListener {
            var sid = binding.etApprovesessionSid.text.toString().toInt()
            var traineruid = binding.etApprovesessionTrainerUid.text.toString().toString().toInt()

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result = ApiManager.approveSession(traineruid,sid)
                binding.tvApprovesessionResult.setText(if(result) "신청 완료" else "신청 실패")
            }
        }

        // 매칭 거절(매칭 삭제)
        binding.btnDisapprovesession.setOnClickListener {
            var sid=binding.etApprovesessionSid.text.toString().toInt()
            var traineruid=binding.etApprovesessionTrainerUid.text.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result = ApiManager.approveSession(traineruid,sid,true);
                binding.tvApprovesessionResult.setText(if(result) "거절 완료" else "거절 실패")
            }
        }

    }

}