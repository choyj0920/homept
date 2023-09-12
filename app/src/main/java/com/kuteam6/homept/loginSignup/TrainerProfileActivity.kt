package com.kuteam6.homept.loginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityTrainerProfileBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TrainerData
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TrainerProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityTrainerProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitTrainerProfile.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var category = ""
                if (binding.purpose1.isChecked) category += "1" else category += "0"
                if (binding.purpose2.isChecked) category += "1" else category += "0"
                if (binding.purpose3.isChecked) category += "1" else category += "0"
                if (binding.purpose4.isChecked) category += "1" else category += "0"
                if (binding.purpose5.isChecked) category += "1" else category += "0"
                if (binding.purpose6.isChecked) category += "1" else category += "0"

                var location = ""
                if (binding.location1.isChecked) location += "서울,"
                if (binding.location2.isChecked) location += "경기,"
                if (binding.location3.isChecked) location += "인천,"
                if (binding.location4.isChecked) location += "강원,"
                if (binding.location5.isChecked) location += "충청,"
                if (binding.location6.isChecked) location += "전라,"
                if (binding.location7.isChecked) location += "경상,"
                if (binding.location8.isChecked) location += "제주,"
                if (location.isNotEmpty())
                    location.substring(location.length-2)

                var name = intent.getStringExtra("name")
                var id = intent.getStringExtra("id")
                var pwd = intent.getStringExtra("pwd")
                var gender = intent.getStringExtra("gender")
                var birth = intent.getStringExtra("birth")

                val pattern = DateTimeFormatter.ofPattern("yyyyMMdd")
                val patterned = birth?.format(pattern)
                val birthDate = LocalDate.parse(patterned, pattern)

                val career = binding.careerHistory.text.toString()
                val lesson = binding.lessionInfo.text.toString()

                var user: UserData = TrainerData(    // 트레이너 일경우 TrainerData생성해서 register함수 매개로
                    name = name.toString(), id = id.toString(), password = pwd.toString(), gender = gender.toString(), birth = birthDate,
                    isTrainee = false, userCategory = category, location = location, career = career, lesson = lesson
                )

                var userData: UserData? = ApiManager.register(user);
                //완료를 누르면 로그인 화면으로
                val intent = Intent(this@TrainerProfileActivity, LoginActivity::class.java)
                startActivity(intent)
            }

        }

    }
}