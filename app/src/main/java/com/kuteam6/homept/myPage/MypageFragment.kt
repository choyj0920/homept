package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kuteam6.homept.databinding.FragmentMypageBinding
import com.kuteam6.homept.restservice.data.UserData


class MypageFragment : Fragment() {

    lateinit var binding: FragmentMypageBinding
    private lateinit var userData: UserData
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater,container, false)

        // 회원명
        //binding.myPageName.text = userData.name

        //내 정보 관리 버튼 이벤트
        binding.btnMyPageInfo.setOnClickListener {
            val myInfointent = Intent(activity, MypageInfoActivity::class.java)
            startActivity(myInfointent)
        }

        // 담당 트레이너/트레이니 버튼 클릭 이벤트
        binding.btnMyPageTrainerTrainee.setOnClickListener {
            val sessionIntent = Intent(activity, MypageMemberListActivity::class.java)
            //사용자의 isTrainee 값 전달
            //memberIntent.putExtra("isTrainee", userData.isTrainee)
            startActivity(sessionIntent)
        }

        // 채팅 버튼 클릭 이벤트

        return binding.root
    }

}