package com.kuteam6.homept.tainerProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityTrainersProfileBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import com.kuteam6.homept.restservice.data.MySession
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.trainerSearch.SearchFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//TODO:리뷰 사진 리사이클러뷰
//TODO: 이력 리사이클러뷰 사이즈 조정해서 스크롤 안되고 다나오게

class TrainersProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainersProfileBinding
    private lateinit var careerAdapter: CareerAdapter
    private lateinit var certificateAdapter: CertificateAdapter

    private var trainerProfile : TrainerProfile? = null

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

        //PT 신청 버튼
        binding.btnPt.setOnClickListener {
            showPtApplyConfirmationDialog()


//            val applyConfirmIntent = Intent(this@TrainersProfileActivity, PtApplyConfirmActivity::class.java)
//            applyConfirmIntent.putExtra("trainerUid", intent.getIntExtra("uid", 0))
//            startActivity(applyConfirmIntent)
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
    private fun showPtApplyConfirmationDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("PT 신청")
        builder.setMessage("PT를 신청하시겠습니까?")

        //"예"버튼
        builder.setPositiveButton("예"){_,_ ->
            initapplysession()
        }

        //"아니요" 버튼
        builder.setNegativeButton("아니요"){dialog, _->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initapplysession() {
        var traineeuid = UserData.userdata?.uid.toString().toInt()
        //트레이너 아이디만 나중에 확인!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        var traineruid = 87


        lifecycleScope.launch(Dispatchers.Main){
            Log.d("sid", MySession.mysession?.sid.toString())
            var result = ApiManager.applySession(traineeuid,traineruid)
            val message = if(result) "신청 완료" else "신청 실패"
            showToastMessage(message)
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}