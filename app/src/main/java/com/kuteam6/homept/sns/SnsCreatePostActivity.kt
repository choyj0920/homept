package com.kuteam6.homept.sns

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.CategoryDialog
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.databinding.ActivitySnsCreatePostBinding
import com.kuteam6.homept.loginSignup.TestActivity
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URL
import java.util.concurrent.Executors

class SnsCreatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnsCreatePostBinding
    private var bitmapImage: Bitmap? = null
    private var selectedimagefile : File? =null

    companion object {
        private const val REQUEST_IMAGE_PICK = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnsCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.getBooleanExtra("isCreate", true)) {
            binding.toolbarBackIv.toolbarBackMainTv.text = "새 게시물"
            binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
                finish()
            }
            binding.toolbarBackIv.toolbarBackSubTv.text = "완료"

            var category : String = "000000"

            binding.snsCreatePostCategoryBtn.setOnClickListener {
                val args = Bundle();
                args.putBoolean("isSelected", false)

                val dialogFragment = CategoryDialog()

                dialogFragment.arguments = args

                dialogFragment.setValueSelectedListener(object : CategoryDialog.OnValueSelectedListener{
                    override fun onValueSelected(value: String) {
                        category = value

                        var resultCategory : String = ""

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

                        binding.snsCreatePostCategoryTv.text = categoryStr
                    }
                })
                dialogFragment.show(supportFragmentManager, "category_dialog")
            }

            binding.toolbarBackIv.toolbarBackSubTv.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    var image = if(selectedimagefile==null) null else ivToFile(binding.snsCreatePostIv)
                    var resultList = ApiManager.createPost(uid = UserData.userdata?.uid!!, title = binding.snsCreatePostTitleEt.text.toString(), content = binding.snsCreatePostContentEt.text.toString(), category = category, imageFile = image)
                }
                finish()
            }

            binding.snsImageBtn.setOnClickListener {
                val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK)
            }
        } else {
            binding.toolbarBackIv.toolbarBackMainTv.text = "게시물 수정"
            binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
                finish()
            }

            binding.toolbarBackIv.toolbarBackSubTv.text = "완료"

            binding.snsCreatePostTitleEt.setText(intent.getStringExtra("title"))
            binding.snsCreatePostContentEt.setText(intent.getStringExtra("content"))


            var category : String = "000000"

            binding.snsCreatePostCategoryBtn.setOnClickListener {
                val args = Bundle();
                args.putBoolean("isSelected", true)
                args.putString("category", intent.getStringExtra("category"));

                val dialogFragment = CategoryDialog()

                dialogFragment.arguments = args

                dialogFragment.setValueSelectedListener(object : CategoryDialog.OnValueSelectedListener{
                    override fun onValueSelected(value: String) {
                        category = value

                        var resultCategory : String = ""

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

                        binding.snsCreatePostCategoryTv.text = categoryStr
                    }
                })
                dialogFragment.show(supportFragmentManager, "category_dialog")
            }

            binding.toolbarBackIv.toolbarBackSubTv.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    var isimagechange = false
                    var image = if(selectedimagefile==null) null else ivToFile(binding.snsCreatePostIv)

                    if (selectedimagefile != null) {
                        isimagechange = true
                    }

                    var resultList = ApiManager.editPost(uid = UserData.userdata?.uid!!,
                        pid = intent.getIntExtra("pid", 0),
                        title = binding.snsCreatePostTitleEt.text.toString(),
                        content = binding.snsCreatePostContentEt.text.toString(),
                        category = category, isimagechange = isimagechange, image)
                }
                finish()
            }

            binding.snsImageBtn.setOnClickListener {
                val pickImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK){
            val imageUri = data?.data
            // uri 객체를 이용하여 파일 경로 생성
            val filePath = getPathFromUri(imageUri)


            // 파일 객체 생성
            selectedimagefile = File(filePath)
            binding.snsCreatePostIv.setImageURI(imageUri)
            Log.d("selectedimagefile", selectedimagefile.toString())

        }
    }

    private fun getPathFromUri(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri!!, projection, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(columnIndex)
        cursor.close()
        return path
    }

    private fun ivToFile(image: ImageView): File {
        var bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)


        // 크기 너무 크게하면 api에서 안받아줌  크기 줄이기 최대 400으로
        val width = bitmap.width
        val height = bitmap.height
        val maxSide = if (width > height) width else height // 가로, 세로 중 큰 쪽 찾기
        val scale = 400f / maxSide // 큰 쪽이 400이 되도록 비율 계산
        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()
        val newbitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)

        var filepath= getExternalFilesDir(null).toString() +"/image"
        val dir= File(filepath)
        if(!dir.exists())
            dir.mkdirs()

        val fileName="temp.png"
        var file = File(dir,fileName)
        filepath=file.absolutePath

        file.writeBitmap(newbitmap, Bitmap.CompressFormat.PNG,50)
        //var file = File(filepath+"/"+fileName)
        file= File(filepath)
        return file
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
            out.close()
        }
    }
}