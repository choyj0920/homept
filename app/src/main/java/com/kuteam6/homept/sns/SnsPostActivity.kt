package com.kuteam6.homept.sns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuteam6.homept.CategoryDialog
import com.kuteam6.homept.HomeActivity
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ActivitySnsPostBinding
import com.kuteam6.homept.databinding.ActivityTrainersProfileBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Postdata
import com.kuteam6.homept.restservice.data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SnsPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySnsPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySnsPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarBackIv.toolbarBackMainTv.text = "새 게시물"
        binding.toolbarBackIv.toolbarBackIv.setOnClickListener {
            val searchTrainerIntent = Intent(this, HomeActivity::class.java)
            searchTrainerIntent.putExtra("fragment", "sns")
            startActivity(searchTrainerIntent)
        }
        binding.toolbarBackIv.toolbarBackSubTv.text = "완료"

        var category : String = "000000"

        binding.snsPostCategoryBtn.setOnClickListener {
            val dialogFragment = CategoryDialog()
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
                var resultList = ApiManager.createPost(uid = UserData.userdata?.uid!!, title = binding.snsPostTitleEt.text.toString(), content = binding.snsPostContentEt.text.toString(), category = category)
            }
            val searchTrainerIntent = Intent(this, HomeActivity::class.java)
            searchTrainerIntent.putExtra("fragment", "sns")
            startActivity(searchTrainerIntent)
        }
    }
}