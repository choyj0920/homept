package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.databinding.ActivityMypageMemberListBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MypageMemberListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMypageMemberListBinding

    val layoutManager = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMypageMemberListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMypageMemberList.toolbarBackMainTv.text = "내 리스트"
        binding.toolbarMypageMemberList.toolbarBackIv.setOnClickListener {
            val memberListIntent = Intent(this, HomeActivity::class.java)

            initGetSession()

            memberListIntent.putExtra("fragment", "mypage")
            startActivity(memberListIntent)
            finish()
        }

    }
    // 내 트레이니/트레이너 리스트 예시
    private fun initGetSession() {

        binding.cbGetSession.setOnCheckedChangeListener { _, isChecked ->
            binding.etGetSessionMyuid.setText("");
            if(isChecked){
                var trainerUid = intent.getIntExtra("trainerUid", TrainerProfile.trainerprofile?.uid?:-1)
                binding.etGetSessionMyuid.setText(trainerUid.toString())
                Log.d("trainerUid", trainerUid.toString())

            } else {
                var traineeUid = intent.getIntExtra("uid", UserData.userdata?.uid?:-1)
                binding.etGetSessionMyuid.setText(traineeUid.toString())
                Log.d("traineeUid", traineeUid.toString())

            }

        }

        binding.btnGetSession.setOnClickListener {

            var uid =binding.etGetSessionMyuid.text.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var resultList =ApiManager.getMySession(binding.cbGetSession.isChecked,uid);
                if(resultList!=null)
                    binding.tvGetSessionResult.setText(resultList.toString())

            }

        }

    }

}