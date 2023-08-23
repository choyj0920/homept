package com.kuteam6.homept.hbtiTest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityHbtiTestBinding
import com.kuteam6.homept.hbtiTest.questionConstants.BulkQuestionConstant
import com.kuteam6.homept.hbtiTest.questionConstants.EnjoyQuestionConstant
import com.kuteam6.homept.hbtiTest.questionConstants.FreeWeightQuestionConstant
import com.kuteam6.homept.hbtiTest.questionConstants.HeavyQuestionConstant
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class HbtiTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHbtiTestBinding

    private var mCurrentPosition: Int = 1
    private var HQuestionList: ArrayList<QuestionData>? = null
    private var BQuestionList: ArrayList<QuestionData>? = null
    private var FQuestionList: ArrayList<QuestionData>? = null
    private var EQuestionList: ArrayList<QuestionData>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHbtiTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        binding.toolbarBackIv.toolbarBackMainTv.text = "HBTI 검사"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        var hScore = 0.0
        var bScore = 0.0
        var fScore = 0.0
        var eScore = 0.0

        val scoreList = arrayListOf<Double>()
        scoreList.add(50.0)
        scoreList.add(50.0)
        scoreList.add(50.0)
        scoreList.add(50.0)

        //리스너
        binding.rgHeavy.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_h1 -> hScore = 12.5
                R.id.rb_h2 -> hScore = 6.25
                R.id.rb_h3 -> hScore = 0.0
                R.id.rb_h4 -> hScore = -6.25
                R.id.rb_h5 -> hScore = -12.5
                null -> hScore = 0.0
            }
        }
        binding.rgBulk.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_b1 -> bScore = 12.5
                R.id.rb_b2 -> bScore = 6.25
                R.id.rb_b3 -> bScore = 0.0
                R.id.rb_b4 -> bScore = -6.25
                R.id.rb_b5 -> bScore = -12.5
                null -> bScore = 0.0
            }
        }
        binding.rgFree.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_f1 -> fScore = 12.5
                R.id.rb_f2 -> fScore = 6.25
                R.id.rb_f3 -> fScore = 0.0
                R.id.rb_f4 -> fScore = -6.25
                R.id.rb_f5 -> fScore = -12.5
                null -> fScore = 0.0
            }
        }
        binding.rgEnjoy.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_e1 -> eScore = 12.5
                R.id.rb_e2 -> eScore = 6.25
                R.id.rb_e3 -> eScore = 0.0
                R.id.rb_e4 -> eScore = -6.25
                R.id.rb_e5 -> eScore = -12.5
                null -> eScore = 0.0
            }
        }


        binding.btnSubmit.setOnClickListener {
            //다음 버튼 -> 1.score 반영  2.다음 question 셋팅  3.라디오버튼 초기화
            mCurrentPosition++

            scoreList[0] += hScore
            scoreList[1] += bScore
            scoreList[2] += fScore
            scoreList[3] += eScore

            Log.d("scoreList", scoreList[0].toString())


            if (mCurrentPosition <= HQuestionList!!.size) {
                setQuestion()
                binding.svHbti.fullScroll(ScrollView.FOCUS_UP)
            }

            uncheckRadio()

            binding.svHbti.fullScroll(ScrollView.FOCUS_UP)

            //제출 버튼 -> 1.서버에 hbti 값 저장  2.result 엑티비티로 score 넘겨주기
            if (mCurrentPosition - 1 == HQuestionList!!.size) {
                //TODO 서버에 hbti 값 저장 (scoreList)

                val uid = UserData.userdata!!.uid

                val scoreListInt = arrayListOf<Int>(0,0,0,0)
                scoreListInt[0] = scoreList[0].toInt()
                scoreListInt[1] = scoreList[1].toInt()
                scoreListInt[2] = scoreList[2].toInt()
                scoreListInt[3] = scoreList[3].toInt()


                //Main은 말 그대로 메인 쓰레드에 대한 Context이며 UI 갱신이나 Toast 등의 View 작업에 사용된다.
                //IO는 네트워킹이나 내부 DB 접근 등 백그라운드에서 필요한 작업을 수행할 때 사용된다.
                //Default는 크기가 큰 리스트를 다루거나 필터링을 수행하는 등 무거운 연산이 필요한 작업에 사용된다.

                CoroutineScope(IO).launch {
                    ApiManager.setHbti(uid,scoreListInt)
                }

                val intent = Intent(this, HbtiResultActivity::class.java)
                intent.putExtra("scoreList", scoreList)
                startActivity(intent)
            }

        }

        HQuestionList = HeavyQuestionConstant.getQuestionList()
        BQuestionList = BulkQuestionConstant.getQuestionList()
        FQuestionList = FreeWeightQuestionConstant.getQuestionList()
        EQuestionList = EnjoyQuestionConstant.getQuestionList()


        setQuestion()

    }

    private fun uncheckRadio() {
        binding.rgHeavy.clearCheck()
        binding.rgBulk.clearCheck()
        binding.rgFree.clearCheck()
        binding.rgEnjoy.clearCheck()
//        binding.rbH1.isChecked = false
//        binding.rbH2.isChecked = false
//        binding.rbH3.isChecked = false
//        binding.rbH4.isChecked = false
//        binding.rbH5.isChecked = false
//
//        binding.rbB1.isChecked = false
//        binding.rbB2.isChecked = false
//        binding.rbB3.isChecked = false
//        binding.rbB4.isChecked = false
//        binding.rbB5.isChecked = false
//
//        binding.rbF1.isChecked = false
//        binding.rbF2.isChecked = false
//        binding.rbF3.isChecked = false
//        binding.rbF4.isChecked = false
//        binding.rbF5.isChecked = false
//
//        binding.rbE1.isChecked = false
//        binding.rbE2.isChecked = false
//        binding.rbE3.isChecked = false
//        binding.rbE4.isChecked = false
//        binding.rbE5.isChecked = false
    }

    private fun setQuestion() {
        val hQue = HQuestionList!![mCurrentPosition - 1]
        val bQue = BQuestionList!![mCurrentPosition - 1]
        val fQue = FQuestionList!![mCurrentPosition - 1]
        val eQue = EQuestionList!![mCurrentPosition - 1]

        binding.tvHQue.text = hQue.question
        binding.tvBQue.text = bQue.question
        binding.tvFQue.text = fQue.question
        binding.tvEQue.text = eQue.question

        if (mCurrentPosition == HQuestionList!!.size) {
            binding.btnSubmit?.text = "제출"
        }
    }


}