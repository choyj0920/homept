package com.kuteam6.homept.trainerRecommend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.FragmentRecommendBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.tainerProfile.TrainersProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecommendFragment : Fragment() {
    lateinit var binding: FragmentRecommendBinding


    var recommendAdapter: RecommendAdapter? = null
    var recommendList = ArrayList<TrainerProfile>()

    private val spinnerList = mutableListOf<Spinner>()

    lateinit var category : String
    var gender : String? = null
    lateinit var location : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommendBinding.inflate(inflater, container, false)

        var typeData = resources.getStringArray(R.array.sportType)
        var genderData = resources.getStringArray(R.array.gender)
        var locationData = resources.getStringArray(R.array.location)

        var typeAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, typeData)
        binding.recommendTypeSp.adapter = typeAdapter

        var genderAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, genderData)
        binding.recommendGenderSp.adapter = genderAdapter

        var locationAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, locationData)
        binding.recommendLacationSp.adapter = locationAdapter

        spinnerList.add(binding.recommendTypeSp)
        spinnerList.add(binding.recommendGenderSp)
        spinnerList.add(binding.recommendLacationSp)

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
                    // 첫 번째 스피너에서 선택된 항목 처리
                    category = selectedItem
                    if(category == "체형교정")
                        category = "100000"
                    else if(category == "근력,체력강화")
                        category = "010000"
                    else if(category == "유아체육")
                        category = "001000"
                    else if(category == "재활")
                        category = "000100"
                    else if(category == "시니어건강")
                        category = "000010"
                    else if(category == "다이어트")
                        category = "000001"
                    else
                        category = ""
                }
                spinnerList[1] -> {
                    // 두 번째 스피너에서 선택된 항목 처리
                    gender = selectedItem
                    if(gender == "남자")
                        gender = "m"
                    else if(gender == "여자")
                        gender = "f"
                    else
                        gender = null
                }
                spinnerList[2] -> {
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
        binding.trainerRecommendBtn.setOnClickListener{
            Log.d("type", category)
            Log.d("gender", gender.toString())
            Log.d("location", location)
            lifecycleScope.launch(Dispatchers.Main) {
                var resultList =
                    ApiManager.searchTrainer(category = category, gender = gender, location = location);
                if(resultList!=null) {
                    recommendList = resultList.toTypedArray().toCollection(ArrayList<TrainerProfile>())

                    for(list in recommendList) {
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
                    recommendAdapter = RecommendAdapter(recommendList)
                    binding.recommendRv.adapter = recommendAdapter
                    binding.recommendRv.layoutManager = LinearLayoutManager(context)
                    recommendAdapter!!.setOnItemClickListener(object : RecommendAdapter.OnItemClickListener{
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
        }
    }
}