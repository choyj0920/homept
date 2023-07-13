package com.kuteam6.homept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityTestBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.ApiService
import com.kuteam6.homept.restservice.data.TraineeData
import com.kuteam6.homept.restservice.data.TrainerData
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 중복 체크 구현
        binding.btnCheckid.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var checkid=binding.etCheckid.text.toString()
                binding.tvCheckid.setText("load...");
                var result = ApiManager.checkIdDupicated(checkid);
                binding.tvCheckid.setText("${ if(result)"아이디중복" else "아이디중복x"}")

            }

        }

        // 회원가입 구현
        binding.btnRegister.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var checkid=binding.etRegisterId.text.toString()
                if(ApiManager.checkIdDupicated(checkid)){
                    binding.tvRegiresult.setText("아이디 중복");
                    return@launch
                }

                binding.tvRegiresult.setText("ing...");

                var user:UserData=
                    if(binding.cbIstrainee.isChecked)   // 트레이니 일경우 TraineeData생성해서 register함수 매개로
                    TraineeData(
                    name = "test용", id = binding.etRegisterId.text.toString(), password = binding.etRegisterpassword.text.toString(), gender = "m", birth = LocalDate.now(),
                    isTrainee = true, userCategory = "100000", description = binding.etTemp1.text.toString()
                ) else
                    TrainerData(    // 트레이너 일경우 TraineeData생성해서 register함수 매개로
                    name = "test용", id = binding.etRegisterId.text.toString(), password = binding.etRegisterpassword.text.toString(), gender = "m", birth = LocalDate.now(),
                    isTrainee = false, userCategory = "100000", career = binding.etTemp1.text.toString(), lesson =  binding.etLesson.text.toString()
                )


                var userData:UserData? =ApiManager.register(user);
                binding.tvRegiresult.setText(userData.toString());

            }
        }

        // 회원가입 -checkbox구현 트레이너트레이니전환
        binding.cbIstrainee.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.tvIstrainer.setText("트레이니")
                binding.etTemp1.hint="description"
                binding.etTemp1.setText("")
                binding.etLesson.setText("")
                binding.etLesson.visibility= View.GONE

            }else{
                binding.tvIstrainer.setText("트레이너")
                binding.etTemp1.hint="이력"
                binding.etTemp1.setText("")
                binding.etLesson.setText("")
                binding.etLesson.visibility= View.VISIBLE
            }
        }

        // 로그인 구현
        binding.btnLogin.setOnClickListener {
            var id = binding.etLoginid.text.toString()
            var password = binding.etLoginpassword.text.toString()
            if (id == "" || password == "") { //빈값x
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var userData:UserData? =ApiManager.login(id, password); // 로그인된 Userdata
                binding.tvLoginresult.setText(userData.toString())



            }
        }


        binding.btnUnregister.setOnClickListener {
            var id = binding.etUnregisterid.text.toString()
            var password = binding.etUnregisterpassword.text.toString()
            if (id == "" || password == "") { //빈값x
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result:Boolean =ApiManager.unRegister(id, password); // 로그인된 Userdata
                binding.tvUnregisterResult.setText(if(result)"탈퇴 완료" else "탈퇴 실패");



            }
        }

    }
}