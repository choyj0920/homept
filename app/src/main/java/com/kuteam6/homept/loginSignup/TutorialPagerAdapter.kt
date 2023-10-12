package com.kuteam6.homept.loginSignup

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kuteam6.homept.R

class TutorialPagerAdapter(fragmentActivity: FragmentActivity) :
FragmentStateAdapter(fragmentActivity){
    private val tutorialPages = listOf(
        TutorialPage("소셜 네트워크",R.drawable.sns, "사람들과 소통을 통해 동기부여를 받고 유용한 정보를 얻을 수 있어요."),
        TutorialPage("HBTI",R.drawable.hbti, "운동 성향 검사를 통해 자신의 성향에 맞는 트레이너를 추천 받을 수 있어요"),
        TutorialPage("PT신청",R.drawable.chat, "개인 정보 노출이 부담스럽다면, 어플에서 바로 PT신청을 할 수 있어요"),
        TutorialPage("이번주, 얼마나 운동했지?",R.drawable.exercise_time, "운동 시간을 기록하여 데이터를 보관할 수 있어요.")
    )

    override fun getItemCount(): Int {
        return tutorialPages.size
    }

    override fun createFragment(position : Int) = TutorialPageFragment().apply {
        arguments = Bundle().apply {
            putString("title", tutorialPages[position].title)
            putInt("imageResId", tutorialPages[position].imageResId)
            putString("description", tutorialPages[position].description)
        }
    }
}