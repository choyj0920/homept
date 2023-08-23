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
            finish()
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

        //내용 출력
        for(i in ptapproveCategory.indices){
            if(ptapproveCategory[0] == '1'){
                binding.etCategory.setText("체형 교정")
            }
            if(ptapproveCategory[1] == '1'){
                binding.etCategory.setText("근력, 체력 강화")
            }
            if(ptapproveCategory[2] == '1'){
                binding.etCategory.setText("유아 체육")
            }
            if(ptapproveCategory[3] == '1'){
                binding.etCategory.setText("재활")
            }
            if(ptapproveCategory[4] == '1'){
                binding.etCategory.setText("시니어 건강")
            }
            if(ptapproveCategory[5] == '1'){
                binding.etCategory.setText("다이어트")
            }
        }

        // 지역
        val location = UserData.userdata?.location.toString()
        binding.etLocation.setText(location)

        //트레이너에 대한 처리
        //매칭 승인
        binding.btnApprovesession.setOnClickListener {
            var sid = MySession.mysession?.sid.toString().toInt()
            binding.etApprovesessionSid.setText(sid)

            var traineruid = UserData.userdata?.uid.toString().toInt()
            binding.etApprovesessionTrainerUid.setText(traineruid)

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result = ApiManager.approveSession(traineruid,sid)
                //Toast 메시지 띄우기
                val message = if(result) "승인 완료" else "승인 실패"
                showToastMessage(message)

            }
        }

        // 매칭 거절(매칭 삭제)
        binding.btnDisapprovesession.setOnClickListener {
            var sid=binding.etApprovesessionSid.text.toString().toInt()
            var traineruid=binding.etApprovesessionTrainerUid.text.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result = ApiManager.approveSession(traineruid,sid,true);
                //Toast 메시지 띄우기
                val message = if(result) "거절 완료" else "거절 실패"
                showToastMessage(message)

            }
        }

    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}