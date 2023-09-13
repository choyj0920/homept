package com.kuteam6.homept.sns

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.CategoryDialog
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.FragmentSnsBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Postdata
import com.kuteam6.homept.restservice.data.TrainerProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SnsFragment : Fragment() {
    lateinit var binding: FragmentSnsBinding

    var snsAdapter: SnsAdapter? = null
    var postDataList = ArrayList<Postdata>()
    var category : String = "000000"

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

                snsAdapter = SnsAdapter(postDataList, requireContext(), lifecycleScope)
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
                        postIntent.putExtra("isImagehave", postdata.isImagehave)
                        startActivity(postIntent)
                    }

                    override fun onUserItemClick(postdata: Postdata) {
                        val userPostIntent = Intent(context, SnsUserPostsActivity::class.java)
                        userPostIntent.putExtra("uid", postdata.uid)
                        userPostIntent.putExtra("isTrainee", postdata.isTrainee)
                        userPostIntent.putExtra("name", postdata.name)
                        startActivity(userPostIntent)
                    }
                })
            }
        }

        return binding.root
    }

    inner class SpinnerItemSelectedListener() : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
            val selectedItem = parent?.getItemAtPosition(position).toString()
            val selectedSpinner = parent as? Spinner
            // 선택된 스피너를 구분하기 위해 스피너를 가져옵니다.

            // 선택된 스피너를 기반으로 원하는 작업을 수행합니다.
            when (selectedSpinner) {
                binding.snsSp -> {
                    lifecycleScope.launch(Dispatchers.Main) {
                        var resultList = ApiManager.getPost(uid = null, category = category)
                        if(resultList!=null) {
                            postDataList = resultList.toTypedArray().toCollection(ArrayList<Postdata>())
                            var dataList = ArrayList<Postdata>()

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

                                if (selectedItem == "트레이너") {
                                    if (!list.isTrainee) {
                                        dataList.add(list)
                                    }
                                } else if (selectedItem == "트레이니") {
                                    if (list.isTrainee) {
                                        dataList.add(list)
                                    }
                                } else {
                                    dataList = postDataList
                                }
                            }

                            snsAdapter = SnsAdapter(dataList, requireContext(), lifecycleScope)
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
                                    postIntent.putExtra("isImagehave", postdata.isImagehave)
                                    startActivity(postIntent)
                                }

                                override fun onUserItemClick(postdata: Postdata) {
                                    val userPostIntent = Intent(context, SnsUserPostsActivity::class.java)
                                    userPostIntent.putExtra("uid", postdata.uid)
                                    userPostIntent.putExtra("isTrainee", postdata.isTrainee)
                                    userPostIntent.putExtra("name", postdata.name)
                                    startActivity(userPostIntent)
                                }
                            })
                        }

                    }
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // 아무것도 선택되지 않았을 때의 동작을 처리할 수 있습니다.
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var idTrainerData = resources.getStringArray(R.array.isTrainer)

        var genderAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, idTrainerData)
        binding.snsSp.adapter = genderAdapter

        binding.snsSp.onItemSelectedListener = SpinnerItemSelectedListener()

        binding.snsSelectCategoryBtn.setOnClickListener {
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

                    binding.snsCategoryTv.text = categoryStr
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

                    snsAdapter = SnsAdapter(postDataList, requireContext(), lifecycleScope)
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
                            postIntent.putExtra("isImagehave", postdata.isImagehave)
                            startActivity(postIntent)
                        }

                        override fun onUserItemClick(postdata: Postdata) {
                            val userPostIntent = Intent(context, SnsUserPostsActivity::class.java)
                            userPostIntent.putExtra("uid", postdata.uid)
                            userPostIntent.putExtra("isTrainee", postdata.isTrainee)
                            userPostIntent.putExtra("name", postdata.name)
                            startActivity(userPostIntent)
                        }
                    })
                }

            }
        }

        binding.snsSearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var tempList = ArrayList<Postdata>()
                if(postDataList != null) {
                    for(list in postDataList) {
                        if(!(p0.toString()=="")) {
                            if(list.content.contains(p0.toString()) || list.title.contains(p0.toString())){
                                tempList.add(list)
                            }
                        }
                    }
                    snsAdapter = SnsAdapter(tempList, requireContext(), lifecycleScope)
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
                            postIntent.putExtra("isImagehave", postdata.isImagehave)
                            startActivity(postIntent)
                        }

                        override fun onUserItemClick(postdata: Postdata) {
                            val userPostIntent = Intent(context, SnsUserPostsActivity::class.java)
                            userPostIntent.putExtra("uid", postdata.uid)
                            userPostIntent.putExtra("isTrainee", postdata.isTrainee)
                            userPostIntent.putExtra("name", postdata.name)
                            startActivity(userPostIntent)
                        }
                    })
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.snsPostBtn.setOnClickListener {
            val postIntent = Intent(context, SnsCreatePostActivity::class.java)
            postIntent.putExtra("isCreate", true)
            startActivity(postIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 데이터를 다시 로드하고 RecyclerView를 새로고침
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

                snsAdapter = SnsAdapter(postDataList, requireContext(), lifecycleScope)
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
                        postIntent.putExtra("isImagehave", postdata.isImagehave)
                        startActivity(postIntent)
                    }

                    override fun onUserItemClick(postdata: Postdata) {
                        val userPostIntent = Intent(context, SnsUserPostsActivity::class.java)
                        userPostIntent.putExtra("uid", postdata.uid)
                        userPostIntent.putExtra("isTrainee", postdata.isTrainee)
                        userPostIntent.putExtra("name", postdata.name)
                        startActivity(userPostIntent)
                    }
                })
            }
        }
    }
}