package com.kuteam6.homept.tainerProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.databinding.ActivityTrainersProfileBinding

//TODO:리뷰 사진 리사이클러뷰
//TODO: 이력 리사이클러뷰 사이즈 조정해서 스크롤 안되고 다나오게

class TrainersProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainersProfileBinding
    private lateinit var careerAdapter: CareerAdapter
    private lateinit var certificateAdapter: CertificateAdapter

    val careerDatas = arrayListOf<CareerData>()
    val certificateDatas = arrayListOf<CertificateData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainersProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDataList()
        initRecyclerView()



        binding.btnPt.setOnClickListener {

        }
    }

    private fun initRecyclerView(){
        val careerAdapter = CareerAdapter(careerDatas)
        binding.rvCareer.adapter = careerAdapter
        binding.rvCareer.layoutManager = LinearLayoutManager(this)

        val certificateAdapter = CertificateAdapter(certificateDatas)
        binding.rvCertificate.adapter = certificateAdapter
        binding.rvCertificate.layoutManager = LinearLayoutManager(this)
    }

    private fun initDataList(){
        with(careerDatas){
            add(CareerData("미스터 올림피아 8회 연속 우승"))
            add(CareerData("한국체육대학교 박사"))
            add(CareerData("트레이닝 경력 8년"))
        }

        with(certificateDatas){
            add(CertificateData("생활체육지도자 1급"))
        }
    }
}