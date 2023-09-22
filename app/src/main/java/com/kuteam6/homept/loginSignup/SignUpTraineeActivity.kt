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
//import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kuteam6.homept.Friend
import com.kuteam6.homept.databinding.ActivitySignupTraineeBinding
import com.kuteam6.homept.restservice.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpTraineeActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupTraineeBinding
    private lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupTraineeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var name: String? = null
        auth = Firebase.auth
        database = Firebase.database.reference

        // 중복 체크 구현
        binding.checkIdBtn.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var checkid = binding.traineeIdEdit.text.toString()
                binding.checkId.setText("load...");
                var result = ApiManager.checkIdDupicated(checkid);
                binding.checkId.setText("${if (result) "아이디중복" else "아이디중복x"}")

            }

        }

        binding.btnSubmit.setOnClickListener {
            if (binding.traineeNameEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "이름을 작성해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.traineeIdEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "아이디를 작성해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.traineePwEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "비밀번호를 작성해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.traineePwCheckEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            } else if (!binding.genderFemale.isChecked && !binding.genderMale.isChecked) {
                Toast.makeText(this, "성별을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.traineeBirthDateEdit.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "생년월일을 작성해주세요", Toast.LENGTH_SHORT).show()
            } else if (binding.traineePwEdit.text.toString() != binding.traineePwCheckEdit.text.toString()) {
                Toast.makeText(this, "입력한 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            } else if (binding.checkId.text.toString() == "아이디중복x") {

                //Firebase
                name = binding.traineeNameEdit.text.toString()
                val email = binding.traineeIdEdit.text.toString() + "@test.com"
                val password = binding.traineePwEdit.text.toString()


                //database.child("test").setValue("test")
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Log.e("task", "success")
                        val user = Firebase.auth.currentUser
                        val userId = user?.uid
                        val userIdSt = userId.toString()
                        Log.d("show uid", userIdSt)

                        val friend = Friend(email.toString(), name.toString(), "null", userIdSt)
                        database.child("users").child(userId.toString()).setValue(friend)
                    }.addOnFailureListener {
                        Log.e("task", "fail")
                    }


                val traineeProfileIntent =
                    Intent(this@SignUpTraineeActivity, TraineeProfileActivity::class.java)
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
            } else {
                Toast.makeText(this, "아이디 중복 확인해주세요", Toast.LENGTH_SHORT).show()
            }

        }

        val passwordEditText = binding.traineePwEdit

        passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordEditText.transformationMethod = null
            } else {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        val passwordEditText2 = binding.traineePwCheckEdit

        passwordEditText2.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                passwordEditText2.transformationMethod = null
            } else {
                passwordEditText2.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        binding.traineeCloseIv.setOnClickListener {
            finish()
        }
    }
}

//auth.createUserWithEmailAndPassword(email.toString(), password.toString())
//.addOnCompleteListener(this) { task ->
//    if (task.isSuccessful) {
//        val user = Firebase.auth.currentUser
//        val userId = user?.uid
//        val userIdSt = userId.toString()
//
//        FirebaseStorage.getInstance()
//            .reference.child("userImages").child("$userIdSt/photo").putFile(imageUri!!).addOnSuccessListener {
//                var userProfile: Uri? = null
//                FirebaseStorage.getInstance().reference.child("userImages").child("$userIdSt/photo").downloadUrl
//                    .addOnSuccessListener {
//                        userProfile = it
//                        Log.d("이미지 URL", "$userProfile")
//                        val friend = Friend(email.toString(), name.toString(), userProfile.toString(), userIdSt)
//                        database.child("users").child(userId.toString()).setValue(friend)
//                    }
//            }