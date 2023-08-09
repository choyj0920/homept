package com.kuteam6.homept.sns

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuteam6.homept.CategoryDialog
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.FragmentRecommendBinding
import com.kuteam6.homept.databinding.FragmentSnsBinding

class SnsFragment : Fragment() {
    lateinit var binding: FragmentSnsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSnsBinding.inflate(inflater, container, false)

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