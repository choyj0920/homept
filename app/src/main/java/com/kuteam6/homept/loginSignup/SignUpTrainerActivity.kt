package com.kuteam6.homept.loginSignup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kuteam6.homept.Friend
import com.kuteam6.homept.databinding.ActivitySignupTrainerBinding
import com.kuteam6.homept.restservice.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpTrainerActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignupTrainerBinding
    private lateinit var auth : FirebaseAuth
    lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var name : String? = null
        auth = Firebase.auth
        database = Firebase.database.reference


        // 중복 체크 구현
        binding.checkIdBtn.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var checkid=binding.trainerIdEdit.text.toString()
                binding.checkId.setText("load...");
                var result = ApiManager.checkIdDupicated(checkid);
                binding.checkId.setText("${ if(result)"아이디중복" else "아이디중복x"}")

            }

        }

        binding.btnSubmit.setOnClickListener {
            if (binding.trainerNameEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "이름을 작성해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.trainerIdEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "아이디를 작성해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.trainerPwEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "비밀번호를 작성해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.trainerPwCheckEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            } else if (!binding.genderFemale.isChecked && !binding.genderMale.isChecked) {
                Toast.makeText(this, "성별을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.trainerBirthDateEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "생년월일을 작성해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.trainerPwEdit.text.toString() != binding.trainerPwCheckEdit.text.toString()) {
                Toast.makeText(this, "입력한 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            } else if (binding.checkId.text == "아이디중복x") {

                //Firebase
                name = binding.trainerNameEdit.text.toString()
                val email = binding.trainerIdEdit.text.toString()
                val password = binding.trainerPwEdit.text.toString()

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
                    if(task.isSuccessful){
                        val user = Firebase.auth.currentUser
                        val userId = user?.uid
                        val userIdSt = userId.toString()


                                val friend = Friend(email.toString(), name.toString(), null, userIdSt)
                                database.child("users").child(userId.toString()).setValue(friend)

                    }
                }



                val traineeProfileIntent = Intent(this@SignUpTrainerActivity, TrainerProfileActivity::class.java)
                traineeProfileIntent.putExtra("name", binding.trainerNameEdit.text.toString())
                traineeProfileIntent.putExtra("id", binding.trainerIdEdit.text.toString())
                traineeProfileIntent.putExtra("pwd", binding.trainerPwEdit.text.toString())
                if (binding.genderFemale.isChecked) {
                    traineeProfileIntent.putExtra("gender", "f")
                } else {
                    traineeProfileIntent.putExtra("gender", "m")
                }
                traineeProfileIntent.putExtra("birth", binding.trainerBirthDateEdit.text.toString())

                startActivity(traineeProfileIntent)
            } else {
                Toast.makeText(this, "아이디 중복 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        val passwordEditText3 = binding.trainerPwEdit

        passwordEditText3.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                passwordEditText3.transformationMethod = null
            }
            else{
                passwordEditText3.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        val passwordEditText4 = binding.trainerPwCheckEdit

        passwordEditText4.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus){
                passwordEditText4.transformationMethod = null
            }
            else{
                passwordEditText4.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        //트레이너로 잘못 들어간 경우 뒤로가기
        binding.trainerCloseIv.setOnClickListener {
            finish()
        }
    }
}