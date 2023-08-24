package com.kuteam6.homept.sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivitySnsPostBinding
import com.kuteam6.homept.databinding.ActivitySnsUserPostsBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Postdata
import com.kuteam6.homept.tainerProfile.TrainersProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SnsUserPostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnsUserPostsBinding

    var snsAdapter: SnsAdapter? = null
    var postDataList = ArrayList<Postdata>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySnsUserPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = intent.getStringExtra("name")
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            finish()
        }
        binding.toolbarBackIv.toolbarBackSubBtn.setImageResource(R.drawable.baseline_contact_page_24)

        if (intent.getBooleanExtra("isTrainee", false)) {
            binding.toolbarBackIv.toolbarBackSubBtn.visibility = View.GONE
        }

        binding.toolbarBackIv.toolbarBackSubBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                var trainerProfile = ApiManager.getTrainerProfile(trainerUid = intent.getIntExtra("uid", 0))

                if (trainerProfile != null) {
                    val trainersProfileIntent = Intent(this@SnsUserPostsActivity, TrainersProfileActivity::class.java)
                    trainersProfileIntent.putExtra("uid", trainerProfile?.uid)
                    trainersProfileIntent.putExtra("name", trainerProfile?.name)
                    trainersProfileIntent.putExtra("gender", trainerProfile?.gender)
                    trainersProfileIntent.putExtra("career", trainerProfile?.career)
                    trainersProfileIntent.putExtra("certificate", trainerProfile?.certificate)
                    trainersProfileIntent.putExtra("lesson", trainerProfile?.lesson)

                    var category : String = ""
                    Log.d("resultCategory", category)

                    if(trainerProfile.usercategory.get(0) == '1')
                        category += "체형교정 / "
                    if(trainerProfile.usercategory.get(1) == '1')
                        category += "근력,체력강화 / "
                    if(trainerProfile.usercategory.get(2) == '1')
                        category += "유아체육 / "
                    if(trainerProfile.usercategory.get(3) == '1')
                        category += "재활 / "
                    if(trainerProfile.usercategory.get(4) == '1')
                        category += "시니어건강 / "
                    if(trainerProfile.usercategory.get(5) == '1')
                        category += "다이어트 / "

                    if (category.isNotEmpty()) {
                        category = category.trim().substring(0, category.length-2)
                    }

                    trainersProfileIntent.putExtra("usercategory", category)
//                            Log.d("certificate", trainerProfile.certificate!!)
//                            Log.d("certificate", trainerProfile.career!!)
                    startActivity(trainersProfileIntent)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            var resultList = ApiManager.getPost(uid = intent.getIntExtra("uid", 0))
            Log.d("post", resultList?.get(0)!!.name)
            if(resultList!=null) {
                postDataList = resultList.toTypedArray().toCollection(ArrayList<Postdata>())

                for(list in postDataList) {
                    var resultCategory : String = ""
                    Log.d("resultCategory", resultCategory)

                    if(list.postcategory.get(0) == '1')
                        resultCategory += "체형교정 / "
                    if(list.postcategory.get(1) == '1')
                        resultCategory += "근력,체력강화 / "
                    if(list.postcategory.get(2) == '1')
                        resultCategory += "유아체육 / "
                    if(list.postcategory.get(3) == '1')
                        resultCategory += "재활 / "
                    if(list.postcategory.get(4) == '1')
                        resultCategory += "시니어건강 / "
                    if(list.postcategory.get(5) == '1')
                        resultCategory += "다이어트 / "

                    if (resultCategory.isNotEmpty()) {
                        resultCategory = resultCategory.trim().substring(0, resultCategory.length-2)
                    }
                    list.postcategory = resultCategory
                }

                snsAdapter = SnsAdapter(postDataList)
                binding.snsUserRv.adapter = snsAdapter
                binding.snsUserRv.layoutManager = LinearLayoutManager(this@SnsUserPostsActivity)
                snsAdapter!!.setOnItemClickListener(object : SnsAdapter.OnItemClickListener{
                    override fun onItemClick(postdata: Postdata) {
                        Log.d("post", postdata.name)
                        val postIntent = Intent(this@SnsUserPostsActivity, SnsPostActivity::class.java)
                        postIntent.putExtra("uid", postdata.uid)
                        postIntent.putExtra("name", postdata.name)
                        postIntent.putExtra("title", postdata.title)
                        postIntent.putExtra("content", postdata.content)
                        postIntent.putExtra("category", postdata.postcategory)
                        postIntent.putExtra("time", postdata.create_at)
                        startActivity(postIntent)
                        Log.d("post2", postdata.name)
                    }

                    override fun onUserItemClick(postdata: Postdata) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }
    }
}