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
import com.kuteam6.homept.restservice.data.TrainerData
import com.kuteam6.homept.restservice.data.TrainerProfile
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
            finish()
        }

        var traineeUid = intent.getIntExtra("uid", UserData.userdata?.uid?:-1)
        binding.etApplysessionTraineeUid.setText(traineeUid.toString())
        Log.d("traineeUid", traineeUid.toString())

        var trainerUid = intent.getIntExtra("trainerUid", -1)
        binding.etApplysessionTrainerUid.setText(trainerUid.toString())
        Log.d("trainerUid", trainerUid.toString())

        initapplySession()

    }

    // 매칭 신청
    private fun initapplySession() {

        binding.btnApplysession.setOnClickListener {

            var traineruid=binding.etApplysessionTrainerUid.text.toString().toInt();
            var traineeuid=binding.etApplysessionTraineeUid.text.toString().toInt();

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result = ApiManager.applySession(traineeuid,traineruid);
                val message = if(result) "신청 완료" else "신청 실패"
                showToastMessage(message)
            }
        }

    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }
}