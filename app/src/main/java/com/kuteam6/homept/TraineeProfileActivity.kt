package com.kuteam6.homept

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityTestBinding
import com.kuteam6.homept.databinding.ActivityTraineeProfileBinding
import com.kuteam6.homept.databinding.ActivityTrainerProfileBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TraineeData
import com.kuteam6.homept.restservice.data.TrainerData
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class TraineeProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityTraineeProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraineeProfileBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_trainee_profile)

        binding.btnSubmitTraineeProfile.setOnClickListener{

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var category = ""
                category += binding.purpose1.isChecked.toString()
                category += binding.purpose2.isChecked.toString()
                category += binding.purpose3.isChecked.toString()
                category += binding.purpose4.isChecked.toString()
                category += binding.purpose5.isChecked.toString()
                category += binding.purpose6.isChecked.toString()

                var user: UserData = TraineeData(
                    name = "test용", id = "binding.etRegisterId.text.toString()", password = "binding.etRegisterpassword.text.toString()", gender = "m", birth = LocalDate.now(),
                    isTrainee = true, userCategory = category, description = binding.injuryHistory.text.toString()
                )

                var userData: UserData? = ApiManager.register(user);
            }

        }
    }
}