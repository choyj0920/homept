package com.kuteam6.homept.tainerProfile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewParent
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityPtApplyBinding

class PtApplyActivity: AppCompatActivity() {

    private lateinit var binding: ActivityPtApplyBinding

    lateinit var ptTime:String
    lateinit var parking:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPtApplyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "PT 신청"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            val ptApplyIntent = Intent(this, TrainersProfileActivity::class.java)
            startActivity(ptApplyIntent)
        }

        setupSpinners()
    }

    private fun setupSpinners() {
        //PT 시간
        ArrayAdapter.createFromResource(this, R.array.ptTime, android.R.layout.simple_spinner_item).also {
            adapter->adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.ptTimeSpinner.adapter = adapter
        }

        //주차 옵션 스피너
        ArrayAdapter.createFromResource(this, R.array.ptTime, android.R.layout.simple_spinner_item).also {
                adapter->adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.ptParkingSpinner.adapter = adapter
        }

        binding.ptTimeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val ptTimeSelected = parent.getItemAtPosition(position).toString()
                // 시간 옵션 선택에 따른 처리 로직 구현
                when(ptTimeSelected){

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 취소되었을 때의 처리 로직 구현 (필요한 경우)
            }
        }

        binding.ptParkingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val ptParkingSelected = parent.getItemAtPosition(position).toString()
                // 주차 옵션 선택에 따른 처리 로직 구현
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // 선택이 취소되었을 때의 처리 로직 구현 (필요한 경우)
            }
        }
    }
}