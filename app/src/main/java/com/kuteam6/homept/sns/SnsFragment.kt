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
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.FragmentRecommendBinding
import com.kuteam6.homept.databinding.FragmentSnsBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Postdata
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.tainerProfile.TrainersProfileActivity
import com.kuteam6.homept.trainerSearch.SearchAdapter
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
                snsAdapter = SnsAdapter(postDataList)
                binding.snsRv.adapter = snsAdapter
                binding.snsRv.layoutManager = LinearLayoutManager(context)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var category : String = "000000"
        Log.d("category", category)

        binding.snsSelectCategoryBtn.setOnClickListener {
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
    }
}