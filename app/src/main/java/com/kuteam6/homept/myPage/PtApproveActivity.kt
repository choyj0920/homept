package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.databinding.ActivityPtApproveBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.TrainerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PtApproveActivity: AppCompatActivity() {
    lateinit var binding: ActivityPtApproveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPtApproveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ptApproveToolbarBackIv.toolbarBackMainTv.text = "PT 승인"
        binding.ptApproveToolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        var sid = intent.getIntExtra("sid", MySession.mysession?.sid?:-1)
        binding.etApprovesessionSid.setText(sid.toString())
        Log.d("sid", sid.toString())

        var trainerUid = intent.getIntExtra("trainerUid", TrainerProfile.trainerprofile?.uid?:-1)
        binding.etApprovesessionTrainerUid.setText(trainerUid.toString())
        Log.d("trainerUid", trainerUid.toString())

        initApplySession()
    }

    private fun initApplySession(){
        //매칭 승인
        binding.btnApprovesession.setOnClickListener {
            var sid = binding.etApprovesessionTrainerUid.text.toString().toInt()
            var traineruid = binding.etApprovesessionTrainerUid.text.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main){
                var result = ApiManager.approveSession(traineruid, sid)
                binding.tvApprovesessionResult.setText(if(result) "승인 완료" else " 승인 실패")
            }
        }

        //매칭 거절(삭제)
        binding.btnDisapprovesession.setOnClickListener {
            var sid = binding.etApprovesessionSid.text.toString().toInt()
            var traineruid = binding.etApprovesessionTrainerUid.text.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main){
                var result = ApiManager.approveSession(traineruid, sid, true)
                binding.tvApprovesessionResult.setText(if(result) "거절 완료" else "거절 실패")
            }
        }
    }
}