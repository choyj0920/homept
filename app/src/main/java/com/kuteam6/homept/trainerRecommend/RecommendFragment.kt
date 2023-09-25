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
import com.kuteam6.homept.CategoryDialog
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.FragmentRecommendBinding
import com.kuteam6.homept.hbtiTest.HbtiStartActivity
import com.kuteam6.homept.myPage.MypageInfoActivity
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.restservice.data.UserData
import com.kuteam6.homept.tainerProfile.TrainersProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecommendFragment : Fragment() {
    lateinit var binding: FragmentRecommendBinding

    private val spinnerList = mutableListOf<Spinner>()

    var gender : String? = null
    lateinit var location : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommendBinding.inflate(inflater, container, false)

        var genderData = resources.getStringArray(R.array.gender)
        var locationData = resources.getStringArray(R.array.location)

        var genderAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, genderData)
        binding.recommendGenderSp.adapter = genderAdapter

        var locationAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, locationData)
        binding.recommendLacationSp.adapter = locationAdapter

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
        var category : String = "000000"
        Log.d("category", category)

        binding.recommendSelectCategoryBtn.setOnClickListener {
            val args = Bundle();
            args.putBoolean("isSelected", false)

            val dialogFragment = CategoryDialog()

            dialogFragment.arguments = args

            dialogFragment.setValueSelectedListener(object : CategoryDialog.OnValueSelectedListener{
                override fun onValueSelected(value: String) {
                    category = value

                    var resultCategory : String = "카테고리:  "

                    if(category.get(0) == '1')
                        resultCategory += "체형교정, "
                    if(category.get(1) == '1')
                        resultCategory += "근력,체력강화, "
                    if(category.get(2) == '1')
                        resultCategory += "유아체육, "
                    if(category.get(3) == '1')
                        resultCategory += "재활, "
                    if(category.get(4) == '1')
                        resultCategory += "시니어건강, "
                    if(category.get(5) == '1')
                        resultCategory += "다이어트, "

                    if (resultCategory.isNotEmpty()) {
                        resultCategory = resultCategory.trim().substring(0, resultCategory.length-2)
                    }
                    val categoryStr = resultCategory

                    binding.recommendCategoryTv.text = categoryStr
                }
            })

            dialogFragment.show(getParentFragmentManager(), "category_dialog")
            Log.d("category2", category)
        }

        binding.btnHbti.setOnClickListener {
            val intentHbti = Intent(activity, HbtiStartActivity::class.java)
            startActivity(intentHbti)
        }

        binding.trainerRecommendBtn.setOnClickListener{
            if (UserData.userdata?.isTrainee!!) {
                if (UserData.userdata?.hbti == null) {
                    val hbtiIntent = Intent(activity, HbtiStartActivity::class.java)
                    startActivity(hbtiIntent)
                } else {
                    val recommendIntent = Intent(activity, RecommendActivity::class.java)
                    recommendIntent.putExtra("category", category)
                    recommendIntent.putExtra("gender", gender)
                    recommendIntent.putExtra("location", location)
                    startActivity(recommendIntent)
                }
            }
        }

        if (!UserData.userdata?.isTrainee!!) {
            binding.trainerRecommendBtn.visibility = View.GONE
            binding.recommendLacationSp.visibility = View.GONE
            binding.recommendGenderSp.visibility = View.GONE
            binding.recommendCategoryTv.visibility = View.GONE
            binding.recommendSelectCategoryBtn.visibility = View.GONE
        }
    }
}