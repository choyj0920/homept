package com.kuteam6.homept.myPage

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.kuteam6.homept.databinding.ActivityMypageTimerBinding
import com.kuteam6.homept.restservice.data.UserData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class MypageTimerActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMypageTimerBinding
    private var pauseOffset: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEnd.visibility = View.GONE
        binding.btnContinue.visibility = View.GONE

        binding.toolbarBackIv.toolbarBackMainTv.text = "운동 시간"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        val todayDateString = SimpleDateFormat("YY년 MM월 dd일 EEE", Locale.getDefault()).format(Date())
        binding.TimerDateTv.text = todayDateString

        //스톱워치가 동작중인지 저장하는 변수, 초기에는 false로 설정
        var isRunning = false

        binding.btnStart.setOnClickListener {
            if(!isRunning){
                // 스톱워치가 동작중이라면 정지시키고 버튼 텍스트 변경
                val baseTime = SystemClock.elapsedRealtime()
                binding.chronometer.base = baseTime - pauseOffset
                binding.chronometer.start()
                isRunning = true

                binding.btnStart.visibility = View.GONE
                binding.btnContinue.visibility = View.VISIBLE
                binding.btnEnd.visibility = View.VISIBLE

                binding.btnContinue.text = "멈춤"
            }
        }

        binding.btnContinue.setOnClickListener {
            if (isRunning){
                pauseOffset = SystemClock.elapsedRealtime() - binding.chronometer.base
                binding.chronometer.stop()
                isRunning = false
                binding.btnContinue.text = "계속하기"
            }
            else{
                val elapsedRealtime = SystemClock.elapsedRealtime() - pauseOffset
                binding.chronometer.base = elapsedRealtime
                isRunning = true
                binding.btnContinue.text = "멈춤"
                binding.chronometer.start()
            }
        }

        binding.btnEnd.setOnClickListener {
            // 스톱워치에서 경과한 시간(밀리초) 계산
            val elapsedMillis = SystemClock.elapsedRealtime() - binding.chronometer.base
            //밀리초를 분으로 변화
            val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)

            isRunning = false
            // 스톱워치를 정지
            binding.chronometer.stop()
            binding.chronometer.base = SystemClock.elapsedRealtime()

            val todayDateString = binding.TimerDateTv.text.toString()

            val database = FirebaseDatabase.getInstance()

            val myRef = database.getReference("exerciseTime").child(UserData.userdata?.uid.toString()).child(todayDateString)

            myRef.setValue(minutes).addOnSuccessListener {
                Toast.makeText(this, "운동 시간이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            }

            //버튼 상태 초기화 하기
            binding.btnStart.visibility = View.VISIBLE;
            binding.btnEnd.visibility = View.GONE;
            binding. btnContinue.visibility = View.GONE;

            pauseOffset = 0L
        }
    }
}