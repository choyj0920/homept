package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.databinding.ActivityMypageMemberListBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.MySession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MypageMemberListActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMypageMemberListBinding

    var sessionAdapter: MySessionAdapter? = null
    var resultList = ArrayList<MySession>()
    val layoutManager = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMypageMemberListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMypageMemberList.toolbarBackMainTv.text = "담당 리스트"
        binding.toolbarMypageMemberList.toolbarBackIv.setOnClickListener {
            finish()
        }

        binding.rvSession.layoutManager = layoutManager
        binding.rvSession.adapter = sessionAdapter
        initGetSession()

        binding.tvGetSessionResult.setOnClickListener {
            val approveIntent = Intent(this, PtApproveActivity::class.java)
            startActivity(approveIntent)
        }
    }

    // 내 트레이니/트레이너 리스트 예시
    private fun initGetSession() {

        binding.cbGetSession.setOnCheckedChangeListener { _, isChecked ->
            binding.etGetSessionMyuid.setText("");
            binding.etGetSessionMyuid.setHint(if(isChecked) "traineruid" else "traineeuid" );

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