package com.kuteam6.homept.loginSignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivitySignupTraineeBinding
import com.kuteam6.homept.restservice.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpTraineeActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupTraineeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTraineeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 중복 체크 구현
        binding.checkIdBtn.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var checkid=binding.traineeIdEdit.text.toString()
                binding.checkId.setText("load...");
                var result = ApiManager.checkIdDupicated(checkid);
                binding.checkId.setText("${ if(result)"아이디중복" else "아이디중복x"}")

            }

        }

        binding.btnSubmit.setOnClickListener {
            val traineeProfileIntent = Intent(this@SignUpTraineeActivity, TraineeProfileActivity::class.java)
            traineeProfileIntent.putExtra("name", binding.traineeNameEdit.text.toString())
            traineeProfileIntent.putExtra("id", binding.traineeIdEdit.text.toString())
            traineeProfileIntent.putExtra("pwd", binding.traineePwEdit.text.toString())
            if (binding.genderFemale.isChecked) {
                traineeProfileIntent.putExtra("gender", "f")
            } else {
                traineeProfileIntent.putExtra("gender", "m")
            }
            traineeProfileIntent.putExtra("birth", binding.traineeBirthDateEdit.text.toString())


            startActivity(traineeProfileIntent)
        }
    }
}