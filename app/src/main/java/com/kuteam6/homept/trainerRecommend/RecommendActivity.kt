package com.kuteam6.homept.trainerRecommend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivityRecommendBinding
import com.kuteam6.homept.databinding.ActivityTrainersProfileBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.tainerProfile.TrainersProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecommendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendBinding
    var recommendList = ArrayList<TrainerProfile>()
    var recommendVPAdapter: RecommendVPAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        binding.toolbarBackIv.toolbarBackMainTv.text = "트레이너 추천"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
//            val searchTrainerIntent = Intent(this, HomeActivity::class.java)
//            searchTrainerIntent.putExtra("fragment", "recommend")
//            startActivity(searchTrainerIntent)
            finish()
        }

        val category = intent.getStringExtra("category")
        val gender = intent.getStringExtra("gender")
        val location = intent.getStringExtra("location")

        // 추천 api 호출
        lifecycleScope.launch(Dispatchers.Main) {
            var resultList =
                ApiManager.searchTrainer(category = category!!, gender = gender, location = location!!);
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
            }
            recommendVPAdapter = RecommendVPAdapter(recommendList)
            binding.recommendVp.adapter = recommendVPAdapter
            binding.recommendVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.recommendTvViewpager.text = getString(R.string.recommend_viewpager_text, 1, recommendList.size)
        }
        binding.recommendVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.recommendTvViewpager.text = getString(R.string.recommend_viewpager_text, position+1, recommendList.size)
            }
        })
    }
}