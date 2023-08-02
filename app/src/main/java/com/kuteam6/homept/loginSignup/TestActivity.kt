package com.kuteam6.homept.loginSignup

import android.R
import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.databinding.ActivityTestBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TraineeData
import com.kuteam6.homept.restservice.data.TrainerData
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*


class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding
    // 로그인 구현 예시

    var isCheckValidate=false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initGetSession()
        initapplySession()
        initRecommendTrainer()
        initSearchTrainer()
        initFindpassword()
        initUnregister()
        initlogin()
        initregister()
        initCheckId()



    }



    // 내 트레이니/트레이너 리스트 예시
    private fun initGetSession() {

        binding.cbGetSession.setOnCheckedChangeListener { _, isChecked ->
            binding.etGetSessionMyuid.setText("");
            binding.etGetSessionMyuid.setHint(if(isChecked) "traineruid" else "traineeuid" );

        }

        binding.btnGetSession.setOnClickListener {

            var uid =binding.etGetSessionMyuid.text.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var resultList =ApiManager.getMySession(binding.cbGetSession.isChecked,uid);
                if(resultList!=null)
                    binding.tvGetSessionResult.setText(resultList.toString())

            }

        }

    }

    // 매칭 신청/ 승인 거절 예시
    private fun initapplySession() {

        binding.btnApplysession.setOnClickListener {

           var traineruid=binding.etApplysessionTrainerUid.text.toString().toInt();
           var traineeuid=binding.etApplysessionTraineeUid.text.toString().toInt();

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result = ApiManager.applySession(traineeuid,traineruid);
                binding.tvApplysessionResult.setText(if(result) "신청완료" else "신청 실패" )

            }
        }
        //매칭 승인
        binding.btnApprovesession.setOnClickListener {
            var sid=binding.etApprovesessionSid.text.toString().toInt()
            var traineruid=binding.etApprovesessionTrainerUid.text.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result = ApiManager.approveSession(traineruid,sid);
                binding.tvApprovesessionResult.setText(if(result) "승인 완료" else "승인 실패" )

            }
        }

        // 매칭 거절( 매칭 삭제)
        binding.btnDisapprovesession.setOnClickListener {
            var sid=binding.etApprovesessionSid.text.toString().toInt()
            var traineruid=binding.etApprovesessionTrainerUid.text.toString().toInt()

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result = ApiManager.approveSession(traineruid,sid,true);
                binding.tvApprovesessionResult.setText(if(result) "거절 완료" else "거절 실패" )

            }
        }

    }



    // 트레이너 추천 예시
    private fun initRecommendTrainer() {

        binding.btnRecommendTrainer.setOnClickListener {

            var strlist =binding.etRecommendTrainerHbti.text.toString().split(",")
            var hbti=strlist.map{it.toInt()}

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var resultList =ApiManager.recommendTrainer(category="000000", gender = null, location = "", hbti);
                if(resultList!=null)
                    binding.tvRecommendTrainerResult.setText(resultList.toString())

            }

        }

    }

    // 트레이너 서치 예시
    private fun initSearchTrainer() {
        val items= listOf("남","여","상관없음");
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter=adapter

        binding.spinner.selectedItem.toString()

        binding.btnSearchTrainer.setOnClickListener {
            var category =binding.etSearchTrainerCategory.text.toString()
            var location = binding.etSearchTrainerLocaton.text.toString()

            if(category.length!=6){
                return@setOnClickListener
            }
            var gender=when(binding.spinner.selectedItemPosition){
                0->"m"
                1->"f"
                else->null
            }
            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var resultList =ApiManager.searchTrainer(category = category, gender = gender, location = location);
                if(resultList!=null)
                    binding.tvSearchTrainerResult.setText(resultList.toString())

            }

        }

    }


    //비밀번호 찾기 예시
    fun initFindpassword(){


        binding.etFindPasswordBirth.apply {
            isFocusable=false
            setOnClickListener {
                showDatePickerDialog()
            }
        }

        // id,name 입력 값에서 수정시 비밀번호 변경버튼 안눌리게
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
                if(result==true){
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
                if(result==true){
                    binding.tvFindpasswordResult.setText("변경 완료")

                }else{
                    binding.tvFindpasswordResult.setText("변경 실패")
                }

            }
        }

    }

    // date picker
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

        // DatePicker의 최소 날짜와 최대 날짜를 설정할 수도 있습니다.
        // datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        // datePickerDialog.datePicker.maxDate = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7) // 7 days from today

        datePickerDialog.show()
    }


    //회원 탈퇴 예시
    fun initUnregister(){
        binding.btnUnregister.setOnClickListener {
            var id = binding.etUnregisterid.text.toString()
            var password = binding.etUnregisterpassword.text.toString()
            if (id == "" || password == "") { //빈값x
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var result:Boolean =ApiManager.unRegister(id, password); // 로그인된 Userdata
                binding.tvUnregisterResult.setText(if(result)"탈퇴 완료" else "탈퇴 실패");



            }
        }
    }

    fun initlogin(){
        binding.btnLogin.setOnClickListener {
            var id = binding.etLoginid.text.toString()
            var password = binding.etLoginpassword.text.toString()
            if (id == "" || password == "") { //빈값x
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var userData:UserData? =ApiManager.login(id, password); // 로그인된 Userdata
                binding.tvLoginresult.setText(userData.toString())



            }
        }
    }

    fun initregister(){
        // 회원가입 구현
        binding.btnRegister.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var checkid=binding.etRegisterId.text.toString()
                if(ApiManager.checkIdDupicated(checkid)){
                    binding.tvRegiresult.setText("아이디 중복");
                    return@launch
                }

                binding.tvRegiresult.setText("ing...");

                var user:UserData=
                    if(binding.cbIstrainee.isChecked)   // 트레이니 일경우 TraineeData생성해서 register함수 매개로
                        TraineeData(
                            name = "test용", id = binding.etRegisterId.text.toString(), password = binding.etRegisterpassword.text.toString(), gender = "m", birth = LocalDate.now(),
                            isTrainee = true, userCategory = "100000", description = binding.etTemp1.text.toString()
                        ) else
                        TrainerData(    // 트레이너 일경우 TraineeData생성해서 register함수 매개로
                            name = "test용", id = binding.etRegisterId.text.toString(), password = binding.etRegisterpassword.text.toString(), gender = "m", birth = LocalDate.now(),
                            isTrainee = false, userCategory = "100000", career = binding.etTemp1.text.toString(), lesson =  binding.etLesson.text.toString(), location = "서울,경기"
                        )


                var userData:UserData? =ApiManager.register(user);
                binding.tvRegiresult.setText(userData.toString());

            }
        }

        // 회원가입 -checkbox구현 트레이너트레이니전환
        binding.cbIstrainee.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.tvIstrainer.setText("트레이니")
                binding.etTemp1.hint="description"
                binding.etTemp1.setText("")
                binding.etLesson.setText("")
                binding.etLesson.visibility= View.GONE

            }else{
                binding.tvIstrainer.setText("트레이너")
                binding.etTemp1.hint="이력"
                binding.etTemp1.setText("")
                binding.etLesson.setText("")
                binding.etLesson.visibility= View.VISIBLE
            }
        }
    }

    fun initCheckId(){
        // 중복 체크 구현
        binding.btnCheckid.setOnClickListener {

            lifecycleScope.launch(Dispatchers.Main) { // 비동기 형태라 외부 쓰레드에서 실행해야함
                var checkid=binding.etCheckid.text.toString()
                binding.tvCheckid.setText("load...");
                var result = ApiManager.checkIdDupicated(checkid);
                binding.tvCheckid.setText("${ if(result)"아이디중복" else "아이디중복x"}")

            }

        }

    }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // 텍스트 변경 전 호출됨
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            // 텍스트 변경 중 호출됨
            binding.btnFindPasswordChange.isEnabled=false;
            binding.tvFindpasswordResult.setText("계정을 확인해주세요")

        }

        override fun afterTextChanged(s: Editable?) {
            // 텍스트 변경 후 호출됨

        }
    }

}