package com.kuteam6.homept

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuteam6.homept.databinding.FragmentMypageBinding


class MypageFragment : Fragment() {

    lateinit var binding: FragmentMypageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater,container, false)

        //내 정보 관리 버튼 이벤트
        binding.btnMyPageInfo.setOnClickListener {
            val intent = Intent(activity, MypageInfoActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

}