package com.kuteam6.homept.sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.kuteam6.homept.CategoryDialog
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.databinding.ActivitySnsCreatePostBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SnsCreatePostActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnsCreatePostBinding

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
                    }
                })
                dialogFragment.show(supportFragmentManager, "category_dialog")
            }

            binding.toolbarBackIv.toolbarBackSubTv.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    Log.d("category2", category)
                    var resultList = ApiManager.createPost(uid = UserData.userdata?.uid!!, title = binding.snsCreatePostTitleEt.text.toString(), content = binding.snsCreatePostContentEt.text.toString(), category = category)
                }
                val searchTrainerIntent = Intent(this, HomeActivity::class.java)
                searchTrainerIntent.putExtra("fragment", "sns")
                startActivity(searchTrainerIntent)
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
                    }
                })
                dialogFragment.show(supportFragmentManager, "category_dialog")
            }

            binding.toolbarBackIv.toolbarBackSubTv.setOnClickListener {
                lifecycleScope.launch(Dispatchers.Main) {
                    Log.d("category2", category)
                    var resultList = ApiManager.editPost(uid = UserData.userdata?.uid!!, title = binding.snsCreatePostTitleEt.text.toString(), content = binding.snsCreatePostContentEt.text.toString(), category = category, pid = intent.getIntExtra("pid", 0))
                }
                val searchTrainerIntent = Intent(this, HomeActivity::class.java)
                searchTrainerIntent.putExtra("fragment", "sns")
                startActivity(searchTrainerIntent)
            }
        }




    }
}