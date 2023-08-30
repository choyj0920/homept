package com.kuteam6.homept

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.kuteam6.homept.trainerSearch.SearchFragment

class CategoryDialog : DialogFragment() {
    private lateinit var valueSelectedListener: OnValueSelectedListener

    interface OnValueSelectedListener {
        fun onValueSelected(value: String)
    }

    fun setValueSelectedListener(listener: OnValueSelectedListener) {
        this.valueSelectedListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // 선택된 아이템 담는 변수
            val selectedItem: ArrayList<String> = arrayListOf()

            // AlertDialog 초기화
            val builder = AlertDialog.Builder(it)

            // 제목 설정
            builder.setTitle("카테고리를 선택하세요")

            val mArgs = arguments
            var isCheckedCategory: BooleanArray? = null
            if (mArgs?.getBoolean("isSelected")!!) {
                isCheckedCategory = BooleanArray(6){i -> false}
                var categorys = mArgs?.getString("category")?.trim()?.split("/")
                for (item in categorys!!) {
                    Log.d("item", item.trim())
                    if (item.trim() == "체형교정") {
                        isCheckedCategory?.set(0, true)
                    }
                    if (item.trim() == "근력,체력강화") {
                        isCheckedCategory?.set(1, true)
                    }
                    if (item.trim() == "유아체육") {
                        isCheckedCategory?.set(2, true)
                    }
                    if (item.trim() == "재활") {
                        isCheckedCategory?.set(3, true)
                    }
                    if (item.trim() == "시니어건강") {
                        isCheckedCategory?.set(4, true)
                    }
                    if (item.trim() == "다이어트") {
                        isCheckedCategory?.set(5, true)
                    }
                }
            }

            // Dialog 멀티 선택 이벤트
            builder.setMultiChoiceItems(R.array.sportType, isCheckedCategory) {
                    p0, which, isChecked ->

                if (mArgs?.getBoolean("isSelected")!!) {
                    isCheckedCategory!![which] = isChecked
                }

                // 데이터 담기
                val categorys: Array<String> = resources.getStringArray(R.array.sportType)

                if(isChecked) {
                    // 추가
                    selectedItem.add(categorys[which])
                } else {
                    // 삭제
                    selectedItem.remove(categorys[which])
                }
            }

            builder.setNegativeButton("선택안함") { dialog, p1 ->
                var category : String = "000000"
                valueSelectedListener?.onValueSelected(category)
            }

            builder.setPositiveButton("Ok") { dialog, p1 ->
                var category : String = "000000"
                for (item in selectedItem) {
                    Log.d("item", item)
                    if (item == "체형교정") {
                        val sb = StringBuilder(category).also { it.setCharAt(0, '1') }
                        category = sb.toString()
                        Log.d("item", category)
                    }
                    if (item == "근력,체력강화") {
                        val sb = StringBuilder(category).also { it.setCharAt(1, '1') }
                        category = sb.toString()
                    }
                    if (item == "유아체육") {
                        val sb = StringBuilder(category).also { it.setCharAt(2, '1') }
                        category = sb.toString()
                    }
                    if (item == "재활") {
                        val sb = StringBuilder(category).also { it.setCharAt(3, '1') }
                        category = sb.toString()
                    }
                    if (item == "시니어건강") {
                        val sb = StringBuilder(category).also { it.setCharAt(4, '1') }
                        category = sb.toString()
                    }
                    if (item == "다이어트") {
                        val sb = StringBuilder(category).also { it.setCharAt(5, '1') }
                        category = sb.toString()
                    }
                }
                Log.d("category4", category)
                valueSelectedListener?.onValueSelected(category)
                dialog.dismiss()
            }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}