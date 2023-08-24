package com.kuteam6.homept.tainerProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityTrainersProfileBinding
import com.kuteam6.homept.restservice.data.UserData
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.trainerSearch.SearchFragment

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

        binding.toolbarBackIv.toolbarBackMainTv.text = "트레이너 프로필"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        Log.d("isTrainee", UserData.userdata?.isTrainee.toString())
        if(!UserData.userdata?.isTrainee!!) {
            binding.btnPt.visibility = View.GONE
        }

        binding.tvName.text = intent.getStringExtra("name")
        binding.tvCategory.text = intent.getStringExtra("usercategory")
        binding.tvLesson.text = intent.getStringExtra("lesson")


//        val career = intent.getStringExtra("career")!!.split("\r?\n|\r".toRegex()).toTypedArray()
//
//        for (careerString in career) {
//            careerDatas.add(CareerData(careerString))
//        }
//
//        val certificate = intent.getStringExtra("certificate")!!.split("\r?\n|\r".toRegex()).toTypedArray()
//
//        for (certificateString in certificate) {
//            certificateDatas.add(CertificateData(certificateString))
//        }

        initDataList()
        initRecyclerView()

        //PT 신청 버튼을 누르면 신청 창으로
        binding.btnPt.setOnClickListener {
            val applyConfirmIntent = Intent(this@TrainersProfileActivity, PtApplyConfirmActivity::class.java)
            applyConfirmIntent.putExtra("trainerUid", intent.getIntExtra("uid", 0))
            startActivity(applyConfirmIntent)
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