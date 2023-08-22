package com.kuteam6.homept.tainerProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityPtApplyConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIv.toolbarBackMainTv.text = "PT 신청"
        binding.backIv.toolbarBackIv.setOnClickListener {
            val ptConfirmIntent = Intent(this@PtApplyConfirmActivity, TrainersProfileActivity::class.java)
            startActivity(ptConfirmIntent)
            finish()
        }

       // Log.d("uid", UserData.userdata?.uid.toString())

        val trainee = intent.getIntExtra("uid", UserData.userdata?.uid?:-1)
        binding.etApplysessionTraineeUid.setText(trainee.toString())

        //setupLayout()
        initapplySession()

    }

    // 매칭 신청
    private fun initapplySession() {

            //트레이니에 대한 처리
            binding.btnApplysession.setOnClickListener {

                var traineruid = UserData.userdata?.uid.toString().toInt()
                binding.etApplysessionTrainerUid.text;

                var traineeuid = UserData.userdata?.uid.toString().toInt()
                binding.etApplysessionTraineeUid.text;

                lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                    var result = ApiManager.applySession(traineeuid,traineruid);
                    //Toast 메시지 띄우기
                    val message = if(result) "신청 완료" else "신청 실패"
                    showToastMessage(message)
                }
            }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
}