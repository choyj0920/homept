package com.kuteam6.homept.loginSignup

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityFindPasswordBinding
import com.kuteam6.homept.restservice.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class FindPwActivity : AppCompatActivity() {

    lateinit var binding: ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivFindPw.setOnClickListener {
            val pwBackIntent = Intent(this, LoginActivity::class.java)
            startActivity(pwBackIntent)
        }

        initFindPassword()
    }

    private fun initFindPassword() {
        binding.findPwBirthEdit.apply {
            isFocusable = false
            setOnClickListener {
                showDatePickerDialog()
            }
        }

        binding.findIdImage.setOnClickListener {
            val intent = Intent(this, FindIdActivity::class.java)
            startActivity(intent)
        }

        binding.btnFindPassword.setOnClickListener {
            val id = binding.findPwIdEdit.text.toString()
            val name = binding.findPwNameEdit.text.toString()

            if(binding.findPwBirthEdit.text.toString()==""){
                return@setOnClickListener
            }
            val birth = LocalDate.parse(binding.findPwBirthEdit.text.toString())
            if(id == "" || name == ""){
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.Main){
                var result: Boolean = ApiManager.checkFindPassword(id, name, birth)

                if(result){
                    binding.btnFindPassword.isEnabled = true
                        //비밀번호 찾기
                }
            }
        }
    }

    private fun showDatePickerDialog(){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
        DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            //선택된 날짜 처리
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)

            //선택된 날짜 사용
            val formatteredDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
            binding.findPwBirthEdit.setText(formatteredDate)
        }, year, month, day)

        datePickerDialog.show()
    }

}