package com.kuteam6.homept.myPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.FragmentMypageBinding
import com.kuteam6.homept.hbtiTest.HbtiStartActivity
import com.kuteam6.homept.restservice.data.UserData


class MypageFragment : Fragment() {

    lateinit var binding: FragmentMypageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater,container, false)

        //프로필 사진 변경
        binding.ivMyPageImage.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireActivity())
            val sheetView = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)

            bottomSheetDialog.setContentView(sheetView)

            val btnTakePhoto = sheetView.findViewById<Button>(R.id.btn_take_photo)
            val btnChooseFromGallery = sheetView.findViewById<Button>(R.id.btn_choose_from_gallery)
            val btnCancelDialog = sheetView.findViewById<Button>(R.id.btn_cancel_dialog)

            btnTakePhoto.setOnClickListener {
                //카메라
                bottomSheetDialog.dismiss()
            }

            btnChooseFromGallery.setOnClickListener {
                //갤러리
                bottomSheetDialog.dismiss()
            }

            btnCancelDialog.setOnClickListener {
                //취소
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.show()
        }

        // 회원명
        val name = UserData.userdata?.name.toString()
        binding.myPageName.text = name

        //내 정보 관리 버튼 이벤트
        binding.btnMyPageInfo.setOnClickListener {
            val myInfointent = Intent(activity, MypageInfoActivity::class.java)
            startActivity(myInfointent)
        }

        // 내 리스트 버튼 클릭 이벤트
        binding.btnMyPageTrainerTrainee.setOnClickListener {
            val sessionIntent = Intent(activity, MypageMemberListActivity::class.java)

            startActivity(sessionIntent)
        }

        // 채팅 버튼 클릭 이벤트

        // HBTI 바로가기
        binding.btnMypageHealthMBTI.setOnClickListener {
            val hbtiIntent = Intent(activity, HbtiStartActivity::class.java)
            startActivity(hbtiIntent)
        }

        Log.d("isTrainee", UserData.userdata?.isTrainee.toString())
        if(UserData.userdata?.isTrainee!!){
            binding.ivPtApplyAlarm.visibility = View.GONE
            binding.btnMyPageCareer.visibility = View.GONE
        }

        return binding.root
    }

}