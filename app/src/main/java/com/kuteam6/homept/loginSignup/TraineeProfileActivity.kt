package com.kuteam6.homept.loginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityTraineeProfileBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TraineeData
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TraineeProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityTraineeProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraineeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmitTraineeProfile.setOnClickListener{

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var category = ""
                if (binding.purpose1.isChecked) category += "1" else category += "0"
                if (binding.purpose2.isChecked) category += "1" else category += "0"
                if (binding.purpose3.isChecked) category += "1" else category += "0"
                if (binding.purpose4.isChecked) category += "1" else category += "0"
                if (binding.purpose5.isChecked) category += "1" else category += "0"
                if (binding.purpose6.isChecked) category += "1" else category += "0"

                val name = intent.getStringExtra("name")
                val id = intent.getStringExtra("id")
                val pwd = intent.getStringExtra("pwd")
                val gender = intent.getStringExtra("gender")
                val birth = intent.getStringExtra("birth")

                val pattern = DateTimeFormatter.ofPattern("yyyyMMdd")
                val patterned = birth?.format(pattern)
                val birthDate = LocalDate.parse(patterned, pattern)

                val injuryHistory = binding.injuryHistory.text.toString()

                var user: UserData = TraineeData(
                    name = name.toString(), id = id.toString(), password = pwd.toString(), gender = gender.toString(), birth = birthDate,
                    isTrainee = true, userCategory = category, description = binding.injuryHistory.text.toString()
                )

                var userData: UserData? = ApiManager.register(user);
            }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}