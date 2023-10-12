package com.kuteam6.homept.myPage

import android.app.Activity

import ProfileFragment
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.kuteam6.homept.Friend
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.FragmentMypageBinding
import com.kuteam6.homept.hbtiTest.HbtiStartActivity
import com.kuteam6.homept.restservice.data.UserData
import java.io.File
import java.util.Calendar


class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding
    private var selectedProfilePhoto : File? = null

    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private val user = Firebase.auth.currentUser
    private val uid = user?.uid.toString()

    companion object{
        private const val GALLERY_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        setProfileImg()

        //프로필 사진
        binding.ivMyPageImage.setOnClickListener {
//            val bottomSheetDialog = BottomSheetDialog(requireActivity())
//            val sheetView = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
//
//            bottomSheetDialog.setContentView(sheetView)
//
//            val btnTakePhoto = sheetView.findViewById<Button>(R.id.btn_take_photo)
//            val btnChooseFromGallery = sheetView.findViewById<Button>(R.id.btn_choose_from_gallery)
//            val btnCancelDialog = sheetView.findViewById<Button>(R.id.btn_cancel_dialog)
//
//            btnTakePhoto.setOnClickListener {
//                //카메라
//                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                startActivityForResult(cameraIntent, GALLERY_REQUEST_CODE)
//                bottomSheetDialog.dismiss()
//            }
//
//            btnChooseFromGallery.setOnClickListener {
//                //갤러리
//                bottomSheetDialog.dismiss()
//            }
//
//            btnCancelDialog.setOnClickListener {
//                //취소
//                bottomSheetDialog.dismiss()
//            }
//
//            bottomSheetDialog.show()

            binding.clSub.visibility = View.GONE
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val profileFragment = ProfileFragment()
            transaction?.replace(R.id.cl_main, profileFragment)
            transaction?.commit()
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
//        binding.btnMyPageChat.setOnClickListener {
//            val chatIntent = Intent(activity, ChatListActivity::class.java)
//            startActivity(chatIntent)
//        }

        // HBTI 바로가기
        binding.btnMypageHealthMBTI.setOnClickListener {
            val hbtiIntent = Intent(activity, HbtiStartActivity::class.java)
            startActivity(hbtiIntent)
        }

        //운동 시간
        binding.btnMyPageTime.setOnClickListener {
            val TimerIntent = Intent(activity, MypageTimerActivity::class.java)
            startActivity(TimerIntent)
        }

        val barChart: BarChart = binding.workoutChart

        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener{
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if(e != null){
                    val workoutTime = (e.y * 60f).toInt() //운동시간

                    Toast.makeText(requireContext(), "$workoutTime 분", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected() {
            }
        })

        //Firebase에서 실시간으로 데이터 읽기
        fireDatabase.child("exerciseTime").child(UserData.userdata?.uid.toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val entries = Array<BarEntry?>(7){index -> BarEntry(index.toFloat(), 0f) }

                snapshot.children.forEach { dataSnapshot ->
                    val timeInMinutes = dataSnapshot.getValue(Long::class.java)
                    if(timeInMinutes != null){
                        val dataParts = dataSnapshot.key?.split(" ")
                            val dayOfWeek = when(dataParts?.get(3)){
                                "Mon" -> 0
                                "Tue" -> 1
                                "Wed" -> 2
                                "Thu" -> 3
                                "Fri" -> 4
                                "Sat" -> 5
                                else -> 6
                            }
                            entries[dayOfWeek] = BarEntry(dayOfWeek.toFloat(),(timeInMinutes / 60f))
                    }
                }

                val barDataSet = BarDataSet(entries.toList(), "Workout Time")
                barDataSet.color = ContextCompat.getColor(requireContext(), R.color.teal_200)
                val barData = BarData(barDataSet)

                with(barChart){
                    data = barData

                    xAxis.valueFormatter = IndexAxisValueFormatter(arrayListOf("월", "화", "수", "목", "금", "토", "일"))
                    xAxis.position = XAxis.XAxisPosition.BOTTOM

                    axisRight.isEnabled = false
                    axisLeft.axisMinimum = 0f
                    axisLeft.axisMaximum = 3f

                    axisLeft.setLabelCount(4, true)

                    description.isEnabled = false

                    invalidate()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MypageFragment", "Failed to read data", error.toException())
            }
        })

        Log.d("isTrainee", UserData.userdata?.isTrainee.toString())
        if (UserData.userdata?.isTrainee!!) {
            binding.ivPtApplyAlarm.visibility = View.GONE
        }

//        binding.btnMyPageChat.setOnClickListener {
//            binding.clSub.visibility = View.GONE
//            val transaction = activity?.supportFragmentManager?.beginTransaction()
//            val fragment = ChatFragment()
//            transaction?.replace(R.id.cl_main, fragment)
//            transaction?.commit()
//        }

        return binding.root
    }

    private fun setProfileImg(){
        val photo = binding.ivMyPageImage
        fireDatabase.child("users").child(uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val userProfile = snapshot.getValue<Friend>()
                println(userProfile)
                Log.d("imageurl",userProfile?.profileImageUrl.toString())
                if(userProfile?.profileImageUrl == "null"){
                    photo!!.setImageResource(R.drawable.empty_profile)
                }else{
                    Glide.with(requireContext()).load(userProfile?.profileImageUrl)
                        .apply(RequestOptions().circleCrop())
                        .into(photo!!)
                }
                Log.d("imageurl2",userProfile?.profileImageUrl.toString())
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            val imageUri = data?.data
            // uri 객체를 이용하여 파일 경로 생성
            //val filePath = getPathFromUri(imageUri)

            // 파일 객체 생성
            //selectedProfilePhoto = File(filePath)
            binding.ivMyPageImage.setImageURI(imageUri)
            Log.d("SelectedProfileImageFile", selectedProfilePhoto.toString())
        }
    }

//    private fun getPathFromUri(uri: Uri?): String {
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = contentResolver.query(uri!!, projection, null, null, null)
//        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        val path = cursor.getString(columnIndex)
//        cursor.close()
//        return path
//    }
//
//    private fun ivToFile(image: ImageView): File {
//        var bitmap = (image.drawable as BitmapDrawable).bitmap
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream)
//
//        // 크기 너무 크게하면 api에서 안 받아줌. 크기 줄이기 최대 400으로
//
//        val width = bitmap.width
//        val height = bitmap.height
//        val maxSide = if(width > height) width else height
//        val scale = 400f /maxSide
//        val newWidth = (width*scale).toInt()
//        val newHeight = (height*scale).toInt()
//        val newBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
//
//        var filepath= getExternalFilesDir(null).toString() +"/image"
//        val dir= File(filepath)
//        if(!dir.exists())
//            dir.mkdirs()
//
//        val fileName="temp.png"
//        var file = File(dir,fileName)
//        filepath=file.absolutePath
//
//        file.writeBitmap(newbitmap, Bitmap.CompressFormat.PNG,50)
//        //var file = File(filepath+"/"+fileName)
//        file= File(filepath)
//        return file
//    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
            out.close()
        }
    }
}