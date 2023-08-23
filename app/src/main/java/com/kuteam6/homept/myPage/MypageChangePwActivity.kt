package com.kuteam6.homept.myPage

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityMypageChangePwBinding
import com.kuteam6.homept.restservice.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

class MypageChangePwActivity : AppCompatActivity() {

    lateinit var binding: ActivityMypageChangePwBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityMypageChangePwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "비밀번호 변경"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        initChangePassword()
    }

    private fun initChangePassword(){
        binding.etFindPasswordBirth.apply {
            isFocusable = false
            setOnClickListener {
                showDatePickerDialog()
            }
        }

        binding.etFindPasswordId.addTextChangedListener(textWatcher)
        binding.etFindPasswordName.addTextChangedListener(textWatcher)
        binding.etFindPasswordBirth.addTextChangedListener(textWatcher)

        // 비밀번호 찾기 예시,
        binding.btnFindPasswordCheck.setOnClickListener {
            val id=binding.etFindPasswordId.text.toString()
            val name=binding.etFindPasswordName.text.toString()
            if(binding.etFindPasswordBirth.text.toString()=="") // 생일 입력 체크
                return@setOnClickListener
            val birth = LocalDate.parse(binding.etFindPasswordBirth.text.toString())
            if(id==""|| name=="")
                return@setOnClickListener

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result :Boolean =  ApiManager.checkFindPassword(id,name,birth)
                if(result){
                    binding.btnFindPasswordChange.isEnabled=true
                    binding.tvFindpasswordResult.setText("계정 확인")


                }

            }
        }
        binding.btnFindPasswordChange.setOnClickListener {

            val id=binding.etFindPasswordId.text.toString()
            val name=binding.etFindPasswordName.text.toString()
            val newPassword=binding.etFindPasswordNewpassword.text.toString()
            if(binding.etFindPasswordBirth.text.toString()=="") // 생일 입력 체크
                return@setOnClickListener
            val birth = LocalDate.parse(binding.etFindPasswordBirth.text.toString())
            if(id==""|| name==""||newPassword=="")
                return@setOnClickListener

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result :Boolean =  ApiManager.changeFindPassword(id,name,birth,newPassword)
                if(result){
                    binding.tvFindpasswordResult.setText("변경 완료")

                }else{
                    binding.tvFindpasswordResult.setText("변경 실패")
                }

            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // 선택된 날짜 처리
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                // 여기에서 선택된 날짜를 사용할 수 있습니다.
                val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
                binding.etFindPasswordBirth.setText(formattedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // 텍스트 변경 전 호출됨
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            // 텍스트 변경 중 호출됨
            binding.btnFindPasswordChange.isEnabled=false;
            binding.tvFindpasswordResult.text = "계정을 확인해주세요"

        }

        override fun afterTextChanged(s: Editable?) {
            // 텍스트 변경 후 호출됨

        }
    }
}