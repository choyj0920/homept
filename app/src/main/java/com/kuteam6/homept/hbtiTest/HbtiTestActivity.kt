package com.kuteam6.homept.hbtiTest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityHbtiTestBinding
import com.kuteam6.homept.hbtiTest.questionConstants.BulkQuestionConstant
import com.kuteam6.homept.hbtiTest.questionConstants.EnjoyQuestionConstant
import com.kuteam6.homept.hbtiTest.questionConstants.FreeWeightQuestionConstant
import com.kuteam6.homept.hbtiTest.questionConstants.HeavyQuestionConstant

class HbtiTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHbtiTestBinding

    private var mCurrentPosition : Int = 1
    private var HQuestionList : ArrayList<QuestionData>? = null
    private var BQuestionList : ArrayList<QuestionData>? = null
    private var FQuestionList : ArrayList<QuestionData>? = null
    private var EQuestionList : ArrayList<QuestionData>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHbtiTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        binding.toolbarBackIv.toolbarBackMainTv.text = "HBTI 검사"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }

        var hScore =0.0
        var bScore =0.0
        var fScore =0.0
        var eScore =0.0

        val scoreList = arrayListOf<Double>()
        scoreList.add(50.0)
        scoreList.add(50.0)
        scoreList.add(50.0)
        scoreList.add(50.0)

        //리스너
        binding.rgHeavy.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_h1 -> hScore = 12.5
                R.id.rb_h2 -> hScore = 6.25
                R.id.rb_h3 -> hScore = 0.0
                R.id.rb_h4 -> hScore = -6.25
                R.id.rb_h5 -> hScore = -12.5
            }
        }
        binding.rgBulk.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_b1 -> bScore = 12.5
                R.id.rb_b2 -> bScore = 6.25
                R.id.rb_b3 -> bScore = 0.0
                R.id.rb_b4 -> bScore = -6.25
                R.id.rb_b5 -> bScore = -12.5
            }
        }
        binding.rgFree.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_f1 -> fScore = 12.5
                R.id.rb_f2 -> fScore = 6.25
                R.id.rb_f3 -> fScore = 0.0
                R.id.rb_f4 -> fScore = -6.25
                R.id.rb_f5 -> fScore = -12.5
            }
        }
        binding.rgEnjoy.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_e1 -> eScore = 12.5
                R.id.rb_e2 -> eScore = 6.25
                R.id.rb_e3 -> eScore = 0.0
                R.id.rb_e4 -> eScore = -6.25
                R.id.rb_e5 -> eScore = -12.5
            }
        }


        binding.btnSubmit.setOnClickListener {
            //다음 버튼 -> 1.score 반영  2.다음 question 셋팅  3.라디오버튼 초기화
            mCurrentPosition++

            scoreList[0]+=hScore
            scoreList[1]+=bScore
            scoreList[2]+=fScore
            scoreList[3]+=eScore

            Log.d("scoreList",scoreList[0].toString())

            setQuestion()

            uncheckRadio()

            //제출 버튼 -> 1.서버에 hbti 값 저장  2.result 엑티비티로 score 넘겨주기
            if(mCurrentPosition-1 == HQuestionList!!.size){
                //TODO 서버에 hbti 값 저장 (scoreList)

                val intent = Intent(this, HbtiResultActivity::class.java)
                intent.putExtra("scoreList",scoreList)
                startActivity(intent)
            }

        }

        HQuestionList = HeavyQuestionConstant.getQuestionList()
        BQuestionList = BulkQuestionConstant.getQuestionList()
        FQuestionList = FreeWeightQuestionConstant.getQuestionList()
        EQuestionList = EnjoyQuestionConstant.getQuestionList()


        setQuestion()

    }

    private fun uncheckRadio(){
        binding.rbH1.isChecked = false
        binding.rbH2.isChecked = false
        binding.rbH3.isChecked = false
        binding.rbH4.isChecked = false
        binding.rbH5.isChecked = false

        binding.rbB1.isChecked = false
        binding.rbB2.isChecked = false
        binding.rbB3.isChecked = false
        binding.rbB4.isChecked = false
        binding.rbB5.isChecked = false

        binding.rbF1.isChecked = false
        binding.rbF2.isChecked = false
        binding.rbF3.isChecked = false
        binding.rbF4.isChecked = false
        binding.rbF5.isChecked = false

        binding.rbE1.isChecked = false
        binding.rbE2.isChecked = false
        binding.rbE3.isChecked = false
        binding.rbE4.isChecked = false
        binding.rbE5.isChecked = false
    }

    private fun setQuestion(){
        val hQue = HQuestionList!![mCurrentPosition - 1]
        val bQue = BQuestionList!![mCurrentPosition - 1]
        val fQue = FQuestionList!![mCurrentPosition - 1]
        val eQue = EQuestionList!![mCurrentPosition - 1]

        binding.tvHQue.text = hQue.question
        binding.tvBQue.text = bQue.question
        binding.tvFQue.text = fQue.question
        binding.tvEQue.text = eQue.question

        if(mCurrentPosition == HQuestionList!!.size){
            binding.btnSubmit?.text = "제출"
        }
    }




}