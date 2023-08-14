package com.kuteam6.homept.trainerRecommend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuteam6.homept.databinding.FragmentRecommendVpBinding
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.restservice.data.UserData

class RecommendVPFragment(private val itemList : ArrayList<TrainerProfile>) : Fragment() {

    lateinit var binding: FragmentRecommendVpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("isTrainee", UserData.userdata?.isTrainee.toString())
        if(!UserData.userdata?.isTrainee!!) {
            binding.btnPt.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommendVpBinding.inflate(inflater, container, false)
        return binding.root
    }

}