package com.kuteam6.homept.trainerRecommend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuteam6.homept.databinding.FragmentRecommendVpBinding
import com.kuteam6.homept.restservice.data.TrainerProfile

class RecommendVPFragment(private val itemList : ArrayList<TrainerProfile>) : Fragment() {

    lateinit var binding: FragmentRecommendVpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommendVpBinding.inflate(inflater, container, false)
        return binding.root
    }

}