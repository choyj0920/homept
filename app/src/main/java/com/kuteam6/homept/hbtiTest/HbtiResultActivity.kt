package com.kuteam6.homept.hbtiTest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.databinding.ActivityHbtiResultBinding
import com.kuteam6.homept.trainerRecommend.RecommendActivity

class HbtiResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHbtiResultBinding
    private var scoreList : ArrayList<Double>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHbtiResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scoreList = intent.getSerializableExtra("scoreList") as ArrayList<Double>

        setView()

        // 툴바 설정
        binding.toolbarBackIv.toolbarBackMainTv.text = "HBTI 검사 결과"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
            val intent = Intent(this, HbtiStartActivity::class.java)
            startActivity(intent)
        }

        //리스너
        binding.btnMove.setOnClickListener {
            finish()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("fragment", "recommend")
            startActivity(intent)
        }
    }

    private fun setView(){
        if(scoreList!![0] >= 50.0){
            binding.tvH.text = "H"
            binding.tvHDes.text = "당신은 고중량, 저반복 운동을 선호하는 헬창이며,"
        }else{
            binding.tvH.text = "L"
            binding.tvHDes.text = "당신은 저중량, 고반복 운동을 선호하는 운동인이며,"
        }
        if(scoreList!![1] >= 50.0){
            binding.tvB.text = "B"
            binding.tvBDes.text = "당신은 근돼 꿈나무입니다."
        }else{
            binding.tvB.text = "D"
            binding.tvBDes.text = "당신은 다이어트를 원합니다."
        }
        if(scoreList!![2] >= 50.0){
            binding.tvF.text = "F"
            binding.tvFDes.text = "또한 머신운동보다 프리웨이트를 좋아하고,"
        }else{
            binding.tvF.text = "M"
            binding.tvFDes.text = "또한 머신운동의 고립감을 좋아하고,"
        }
        if(scoreList!![3] >= 50.0){
            binding.tvE.text = "E"
            binding.tvEDes.text = "고통을 즐기는 변태입니다."
        }else{
            binding.tvE.text = "S"
            binding.tvEDes.text = "강압갑을 즐기는 변태입니다."
        }



    }
}