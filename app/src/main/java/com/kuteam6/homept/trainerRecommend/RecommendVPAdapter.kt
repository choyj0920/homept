package com.kuteam6.homept.trainerRecommend

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.databinding.FragmentRecommendVpBinding
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Review
import com.kuteam6.homept.restservice.data.TrainerProfile
import com.kuteam6.homept.restservice.data.UserData
import com.kuteam6.homept.tainerProfile.ReviewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecommendVPAdapter(private val itemList : ArrayList<TrainerProfile>, private val context: Context, private val lifecycleCoroutineScope: LifecycleCoroutineScope) : RecyclerView.Adapter<RecommendVPAdapter.ViewHolder>() {
    lateinit var binding: FragmentRecommendVpBinding
    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(trainerProfile: TrainerProfile)
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : FragmentRecommendVpBinding) : RecyclerView.ViewHolder(binding.root) {
        var reviewDatas = arrayListOf<Review>()

        fun bind(trainerProfile: TrainerProfile) {
            binding.recommendTvName.text = trainerProfile.name
            binding.recommendTvCategory.text = trainerProfile.usercategory
            binding.recommendTvLesson.text = trainerProfile.lesson

            Log.d("isTrainee", UserData.userdata?.isTrainee.toString())
            if(!UserData.userdata?.isTrainee!!) {
                binding.btnPt.visibility = View.GONE
            }

            lifecycleCoroutineScope.launch(Dispatchers.Main) {
                val reviewList =  ApiManager.getReview(trainerProfile.uid)
                if (reviewList != null) {
                    reviewDatas = reviewList.toTypedArray().toCollection(ArrayList<Review>())
                    val reviewAdapter = ReviewAdapter(reviewDatas)
                    binding.trainerReviewRv.adapter = reviewAdapter
                    binding.trainerReviewRv.layoutManager = LinearLayoutManager(context)
                }
            }

            binding.btnPt.setOnClickListener {
                itemClickListener.onItemClick(trainerProfile)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = FragmentRecommendVpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

}