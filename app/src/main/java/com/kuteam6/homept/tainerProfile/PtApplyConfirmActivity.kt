package com.kuteam6.homept.tainerProfile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityPtApplyConfirmBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PtApplyConfirmActivity: AppCompatActivity() {
    lateinit var binding: ActivityPtApplyConfirmBinding
    private lateinit var userData: UserData

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityPtApplyConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIv.toolbarBackMainTv.text = "PT 신청"
        binding.backIv.toolbarBackIv.setOnClickListener {

            val ptConfirmIntent = Intent(this@PtApplyConfirmActivity, PtApplyActivity::class.java)
            startActivity(ptConfirmIntent)
        }

        //setupLayout()
        initapplySession()
    }

    //트레이너일 때, 트레이니일 때 레이아웃 나누기
    private fun setupLayout() {
        if (userData.isTrainee) {
            binding.traineeLayout.visibility = View.VISIBLE
            binding.trainerLayout.visibility = View.GONE
        } else {
            binding.traineeLayout.visibility = View.GONE
            binding.trainerLayout.visibility = View.VISIBLE
        }
    }

    // 매칭 신청/ 승인 거절 예시
    private fun initapplySession() {

            //트레이니에 대한 처리
            binding.btnApplysession.setOnClickListener {

                var traineruid=binding.etApplysessionTrainerUid.text.toString().toInt();
                var traineeuid=binding.etApplysessionTraineeUid.text.toString().toInt();

                lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                    var result = ApiManager.applySession(traineeuid,traineruid);
                    //Toast 메시지 띄우기
                    val message = if(result) "신청 완료" else "신청 실패"
                    showToastMessage(message)
                }
            }

            //트레이너에 대한 처리
            //매칭 승인
            binding.btnApprovesession.setOnClickListener {
                var sid=binding.etApprovesessionSid.text.toString().toInt()
                var traineruid=binding.etApprovesessionTrainerUid.text.toString().toInt()

                lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                    var result = ApiManager.approveSession(traineruid,sid)
                    //Toast 메시지 띄우기
                    val message = if(result) "승인 완료" else "승인 실패"
                    showToastMessage(message)

                }
            }

            // 매칭 거절( 매칭 삭제)
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
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
}