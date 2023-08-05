package com.kuteam6.homept.trainerSearch

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.CategoryDialog
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.FragmentSearchBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.tainerProfile.TrainersProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Objects


class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding

    var searchAdapter: SearchAdapter? = null
    var searchList = ArrayList<TrainerProfile>()

    private val spinnerList = mutableListOf<Spinner>()

    //lateinit var category : String
    var gender : String? = null
    lateinit var location : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        var genderData = resources.getStringArray(R.array.gender)
        var locationData = resources.getStringArray(R.array.location)

        var genderAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, genderData)
        binding.searchGenderSp.adapter = genderAdapter

        var locationAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, locationData)
        binding.searchLacationSp.adapter = locationAdapter

        spinnerList.add(binding.searchGenderSp)
        spinnerList.add(binding.searchLacationSp)

        for(spinner in spinnerList) {
            spinner.onItemSelectedListener = SpinnerItemSelectedListener()
        }

        return binding.root
    }

    inner class SpinnerItemSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
            val selectedItem = parent?.getItemAtPosition(position).toString()
            val selectedSpinner = parent as? Spinner
            // 선택된 스피너를 구분하기 위해 스피너를 가져옵니다.

            // 선택된 스피너를 기반으로 원하는 작업을 수행합니다.
            when (selectedSpinner) {
                spinnerList[0] -> {
                    // 두 번째 스피너에서 선택된 항목 처리
                    gender = selectedItem
                    if(gender == "남자")
                        gender = "m"
                    else if(gender == "여자")
                        gender = "f"
                    else
                        gender = null
                }
                spinnerList[1] -> {
                    // 두 번째 스피너에서 선택된 항목 처리
                    location = selectedItem
                    if(location == "위치")
                        location = ""
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // 아무것도 선택되지 않았을 때의 동작을 처리할 수 있습니다.
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var category : String = "000000"
        Log.d("category", category)

        binding.searchSelectCategoryBtn.setOnClickListener {
            val dialogFragment = CategoryDialog()
            dialogFragment.setValueSelectedListener(object : CategoryDialog.OnValueSelectedListener{
                override fun onValueSelected(value: String) {
                    category = value
                    Log.d("category1", category)
                }
            })
            dialogFragment.show(getParentFragmentManager(), "category_dialog")
            Log.d("category2", category)
        }

        binding.treainerSearchEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var tempList = ArrayList<TrainerProfile>()
                if(searchList!=null) {
                    for(list in searchList) {
                        if(!(p0.toString()=="")) {
                            Log.d("p0", p0.toString())
                            Log.d("name", list.name)
                            if(list.name.contains(p0.toString())){
                                Log.d("bool", list.name.contains(p0.toString()).toString())
                                tempList.add(list)
                                Log.d("temp", tempList.get(0).name)
                            }
                        }
                    }
                    searchAdapter = SearchAdapter(tempList)
                    binding.searchRv.adapter = searchAdapter
                    binding.searchRv.layoutManager = LinearLayoutManager(context)
                    searchAdapter!!.setOnItemClickListener(object : SearchAdapter.OnItemClickListener{
                        override fun onItemClick(trainerProfile: TrainerProfile) {
                            val trainersProfileIntent = Intent(context, TrainersProfileActivity::class.java)
                            trainersProfileIntent.putExtra("name", trainerProfile.name)
                            trainersProfileIntent.putExtra("gender", trainerProfile.gender)
                            trainersProfileIntent.putExtra("career", trainerProfile.career)
                            trainersProfileIntent.putExtra("certificate", trainerProfile.certificate)
                            trainersProfileIntent.putExtra("lesson", trainerProfile.lesson)
                            trainersProfileIntent.putExtra("usercategory", trainerProfile.usercategory)
//                            Log.d("certificate", trainerProfile.certificate!!)
//                            Log.d("certificate", trainerProfile.career!!)
                            startActivity(trainersProfileIntent)
                        }
                    })
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.trainerSearchBtn.setOnClickListener{
            if(binding.treainerSearchEt.text.toString()=="") {
                Log.d("gender", gender.toString())
                Log.d("location", location)
                lifecycleScope.launch(Dispatchers.Main) {
                    var resultList =ApiManager.searchTrainer(category = category, gender = gender, location = location);
                    if(resultList!=null) {
                        searchList = resultList.toTypedArray().toCollection(ArrayList<TrainerProfile>())

                        for(list in searchList) {
                            if(list.gender == "f")
                                list.gender = "여자"
                            else if(list.gender == "m")
                                list.gender = "남자"

                            var category : String = ""

                            if(list.usercategory.get(0) == '1')
                                category += "체형교정, "
                            if(list.usercategory.get(1) == '1')
                                category += "근력,체력강화, "
                            if(list.usercategory.get(2) == '1')
                                category += "유아체육, "
                            if(list.usercategory.get(3) == '1')
                                category += "재활, "
                            if(list.usercategory.get(4) == '1')
                                category += "시니어건강, "
                            if(list.usercategory.get(5) == '1')
                                category += "다이어트, "

                            category = category.trim().substring(0, category.length-2)
                            list.usercategory = category
                        }
                        searchAdapter = SearchAdapter(searchList)
                        binding.searchRv.adapter = searchAdapter
                        binding.searchRv.layoutManager = LinearLayoutManager(context)
                        searchAdapter!!.setOnItemClickListener(object : SearchAdapter.OnItemClickListener{
                            override fun onItemClick(trainerProfile: TrainerProfile) {
                                val trainersProfileIntent = Intent(context, TrainersProfileActivity::class.java)
                                trainersProfileIntent.putExtra("name", trainerProfile.name)
                                trainersProfileIntent.putExtra("gender", trainerProfile.gender)
                                trainersProfileIntent.putExtra("career", trainerProfile.career)
                                trainersProfileIntent.putExtra("certificate", trainerProfile.certificate)
                                trainersProfileIntent.putExtra("lesson", trainerProfile.lesson)
                                trainersProfileIntent.putExtra("usercategory", trainerProfile.usercategory)
//                                Log.d("certificate", trainerProfile.certificate!!)
//                                Log.d("certificate", trainerProfile.career!!)
                                startActivity(trainersProfileIntent)
                            }
                        })
                    }
                }
            }else {
                searchTrainer(category)
            }

        }
    }

    private fun searchTrainer(category:String) {
        lifecycleScope.launch(Dispatchers.Main) {
            var resultList =ApiManager.searchTrainer(category = category, gender = gender, location = location);
            if(resultList!=null) {
                searchList = resultList.toTypedArray().toCollection(ArrayList<TrainerProfile>())

                for(list in searchList) {
                    if(list.gender == "f")
                        list.gender = "여자"
                    else if(list.gender == "m")
                        list.gender = "남자"

                    var category : String = ""

                    if(list.usercategory.get(0) == '1')
                        category += "체형교정, "
                    if(list.usercategory.get(1) == '1')
                        category += "근력,체력강화, "
                    if(list.usercategory.get(2) == '1')
                        category += "유아체육, "
                    if(list.usercategory.get(3) == '1')
                        category += "재활, "
                    if(list.usercategory.get(4) == '1')
                        category += "시니어건강, "
                    if(list.usercategory.get(5) == '1')
                        category += "다이어트, "

                    category = category.trim().substring(0, category.length-2)
                    list.usercategory = category
                }
                var tempList = ArrayList<TrainerProfile>()
                if(searchList!=null) {
                    for(list in searchList) {
                        if(!(binding.treainerSearchEt.text.toString()=="")) {
                            if(list.name.contains(binding.treainerSearchEt.text.toString())){
                                tempList.add(list)
                            }
                        }
                    }
                    searchAdapter = SearchAdapter(tempList)
                    binding.searchRv.adapter = searchAdapter
                    binding.searchRv.layoutManager = LinearLayoutManager(context)
                    searchAdapter!!.setOnItemClickListener(object : SearchAdapter.OnItemClickListener{
                        override fun onItemClick(trainerProfile: TrainerProfile) {
                            val trainersProfileIntent = Intent(context, TrainersProfileActivity::class.java)
                            trainersProfileIntent.putExtra("name", trainerProfile.name)
                            trainersProfileIntent.putExtra("gender", trainerProfile.gender)
                            trainersProfileIntent.putExtra("career", trainerProfile.career)
                            trainersProfileIntent.putExtra("certificate", trainerProfile.certificate)
                            trainersProfileIntent.putExtra("lesson", trainerProfile.lesson)
                            trainersProfileIntent.putExtra("usercategory", trainerProfile.usercategory)
//                            Log.d("certificate", trainerProfile.certificate!!)
//                            Log.d("certificate", trainerProfile.career!!)
                            startActivity(trainersProfileIntent)
                        }
                    })
                }
            }
        }
    }
}