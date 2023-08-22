package com.kuteam6.homept.myPage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.databinding.ActivityMypageNameEditBinding
import com.kuteam6.homept.restservice.data.UserData

class MypageEditnameActivity: AppCompatActivity() {

    lateinit var binding: ActivityMypageNameEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageNameEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameEditIv.toolbarBackMainTv.text = "이름 수정"
        binding.nameEditIv.toolbarBackIv.setOnClickListener {
            val nameEditIntent = Intent(this, MypageInfoActivity::class.java)
            startActivity(nameEditIntent)
        }

        val beforeName = UserData.userdata?.name.toString()
        binding.tvNowName.text = beforeName

        binding.btnNameEdit.setOnClickListener {
            binding.btnNameEdit.setOnClickListener {
                val modifiedName = binding.etAfterName.text.toString()
                val returnIntent = Intent()
                returnIntent.putExtra("afterName", modifiedName)
                setResult(Activity.RESULT_OK, returnIntent)

                val nameEditCompleteIntent = Intent(this, MypageInfoActivity::class.java)
                startActivity(nameEditCompleteIntent)
                finish()
            }
        }
    }
}