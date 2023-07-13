package com.kuteam6.homept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityTraineeProfileBinding
import com.kuteam6.homept.databinding.ActivityTrainerProfileBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TraineeData
import com.kuteam6.homept.restservice.data.TrainerData
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class TrainerProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityTrainerProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainerProfileBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_trainer_profile)

        binding.btnSubmitTraineeProfile.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var category = ""
                category += binding.purpose1.isChecked.toString()
                category += binding.purpose2.isChecked.toString()
                category += binding.purpose3.isChecked.toString()
                category += binding.purpose4.isChecked.toString()
                category += binding.purpose5.isChecked.toString()
                category += binding.purpose6.isChecked.toString()

                var name = intent.getStringExtra("name")
                var id = intent.getStringExtra("id")
                var pwd = intent.getStringExtra("pwd")
                var gender = intent.getStringExtra("gender")
                var birth = intent.getStringExtra("birth")



                var user: UserData = TrainerData(    // 트레이너 일경우 TrainerData생성해서 register함수 매개로
                    name = name.toString(), id = id.toString(), password = pwd.toString(), gender = gender.toString(), birth = LocalDate.parse(birth),
                    isTrainee = false, userCategory = category, career = binding.careerHistory.text.toString(), lesson =  binding.lessionInfo.text.toString()
                )

                var userData: UserData? = ApiManager.register(user);
                binding.tvRegiresult.setText(userData.toString());
            }

        }
    }
}