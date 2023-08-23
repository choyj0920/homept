package com.kuteam6.homept.sns

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.CategoryDialog
import com.kuteam6.homept.databinding.FragmentSnsBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Postdata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SnsFragment : Fragment() {
    lateinit var binding: FragmentSnsBinding

    var snsAdapter: SnsAdapter? = null
    var postDataList = ArrayList<Postdata>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSnsBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.Main) {
            var resultList = ApiManager.getPost(uid = null)
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
                binding.snsRv.adapter = snsAdapter
                binding.snsRv.layoutManager = LinearLayoutManager(context)
                snsAdapter!!.setOnItemClickListener(object : SnsAdapter.OnItemClickListener{
                    override fun onItemClick(postdata: Postdata) {
                        val postIntent = Intent(context, SnsPostActivity::class.java)
                        postIntent.putExtra("uid", postdata.uid)
                        postIntent.putExtra("pid", postdata.pid)
                        postIntent.putExtra("name", postdata.name)
                        postIntent.putExtra("title", postdata.title)
                        postIntent.putExtra("content", postdata.content)
                        postIntent.putExtra("category", postdata.postcategory)
                        postIntent.putExtra("time", postdata.create_at)
                        startActivity(postIntent)
                    }

                    override fun onUserItemClick(postdata: Postdata) {
                        val userPostIntent = Intent(context, SnsUserPostsActivity::class.java)
                        userPostIntent.putExtra("uid", postdata.uid)
                        userPostIntent.putExtra("name", postdata.name)
                        startActivity(userPostIntent)
                    }
                })
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var category : String = "000000"
        Log.d("category", category)

        binding.snsSelectCategoryBtn.setOnClickListener {
            val args = Bundle();
            args.putBoolean("isSelected", false)

            val dialogFragment = CategoryDialog()
            dialogFragment.arguments = args

            dialogFragment.setValueSelectedListener(object : CategoryDialog.OnValueSelectedListener{
                override fun onValueSelected(value: String) {
                    category = value
                    Log.d("category1", category)
                }
            })
            dialogFragment.show(getParentFragmentManager(), "category_dialog")
            Log.d("category2", category)
        }

        binding.snsSearchBtn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                var resultList = ApiManager.getPost(uid = null, category = category)
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
                    binding.snsRv.adapter = snsAdapter
                    binding.snsRv.layoutManager = LinearLayoutManager(context)
                    snsAdapter!!.setOnItemClickListener(object : SnsAdapter.OnItemClickListener{
                        override fun onItemClick(postdata: Postdata) {
                            val postIntent = Intent(context, SnsPostActivity::class.java)
                            postIntent.putExtra("uid", postdata.uid)
                            postIntent.putExtra("pid", postdata.pid)
                            postIntent.putExtra("name", postdata.name)
                            postIntent.putExtra("title", postdata.title)
                            postIntent.putExtra("content", postdata.content)
                            postIntent.putExtra("category", postdata.postcategory)
                            postIntent.putExtra("time", postdata.create_at)
                            startActivity(postIntent)
                        }

                        override fun onUserItemClick(postdata: Postdata) {
                            val userPostIntent = Intent(context, SnsUserPostsActivity::class.java)
                            userPostIntent.putExtra("uid", postdata.uid)
                            userPostIntent.putExtra("name", postdata.name)
                            startActivity(userPostIntent)
                        }
                    })
                }

            }
        }

        binding.snsPostBtn.setOnClickListener {
            val postIntent = Intent(context, SnsCreatePostActivity::class.java)
            postIntent.putExtra("isCreate", true)
            startActivity(postIntent)
        }
    }
}