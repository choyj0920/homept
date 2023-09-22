package com.kuteam6.homept.loginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kuteam6.homept.Friend
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
    private lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTraineeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        database = Firebase.database.reference

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
                    isTrainee = true, userCategory = category, description = injuryHistory
                )

                var userData: UserData? = ApiManager.register(user);

                //Firebase
                val fname = name.toString()
                val email = id.toString() + "@test.com"
                val password = pwd.toString()


                //database.child("test").setValue("test")
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Log.e("task", "success")
                        val user = Firebase.auth.currentUser
                        val userId = user?.uid
                        val userIdSt = userId.toString()
                        val suid = userData!!.uid
                        Log.d("show uid", userIdSt)

                        val friend = Friend(email.toString(), fname.toString(), "null", userIdSt, suid)
                        database.child("users").child(userId.toString()).setValue(friend)
                    }.addOnFailureListener {
                        Log.e("task", "fail")
                    }

                val intent = Intent(this@TraineeProfileActivity, LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }
}