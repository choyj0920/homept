package com.kuteam6.homept.loginSignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.kuteam6.homept.databinding.ActivityMainBinding
import com.kuteam6.homept.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {



    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

//private var imageUri : Uri? = null
//
////이미지 등록
//private val getContent =
//    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        if(result.resultCode == RESULT_OK) {
//            imageUri = result.data?.data //이미지 경로 원본
//            registration_iv.setImageURI(imageUri) //이미지 뷰를 바꿈
//            Log.d("이미지", "성공")
//        }
//        else{
//            Log.d("이미지", "실패")
//        }
//    }
