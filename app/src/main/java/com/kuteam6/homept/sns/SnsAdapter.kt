package com.kuteam6.homept.sns

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kuteam6.homept.R
import com.kuteam6.homept.databinding.ItemSnsBinding
import com.kuteam6.homept.loginSignup.TestActivity
import com.kuteam6.homept.restservice.ApiManager
import com.kuteam6.homept.restservice.data.Postdata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL
import java.util.concurrent.Executors
import com.bumptech.glide.Glide

class SnsAdapter(private val itemList : ArrayList<Postdata>, private val context: Context, private val lifecycleScope: CoroutineScope) : RecyclerView.Adapter<SnsAdapter.ViewHolder>() {
    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(postdata: Postdata)
        fun onUserItemClick(postdata: Postdata)
    }

    fun setOnItemClickListener(onItemClickListener : OnItemClickListener) {
        itemClickListener = onItemClickListener
    }

    inner class ViewHolder(val binding : ItemSnsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(postdata: Postdata) {
            // binding.snsProfileIv.setImageResource()
            binding.snsNameTv.text = postdata.name
            binding.snsTitleTv.text = postdata.title
            binding.snsContentTv.text = postdata.content
            binding.snsTypeTv.text = postdata.postcategory
            binding.snsUploadDateTv.text = postdata.create_at

            if (postdata.isTrainee) {
                binding.snsNameTv.setTextColor(ContextCompat.getColor(context, R.color.orange))
            } else {
                binding.snsNameTv.setTextColor(ContextCompat.getColor(context, R.color.green))
            }

            lifecycleScope.launch(Dispatchers.Main) {
                if (postdata.isImagehave == 1) {
                    val imageId = ApiManager.getSnsImage(postdata.pid)
                    Glide.with(context)
                        .load(imageId)
                        .into(binding.snsImageIv)
                } else {
                    binding.snsImageIv.visibility = View.GONE
                }
            }

            binding.itemSnsCl.setOnClickListener {
                itemClickListener.onItemClick(postdata)
            }
            binding.snsProfileIv.setOnClickListener {
                itemClickListener.onUserItemClick(postdata)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val binding = ItemSnsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

}