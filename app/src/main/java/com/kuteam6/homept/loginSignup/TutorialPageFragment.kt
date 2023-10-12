package com.kuteam6.homept.loginSignup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kuteam6.homept.R

class TutorialPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tutorial_page,container,false)

        arguments?.let {
            val title = it.getString("title")
            val imageResId = it.getInt("imageResId")
            val description = it.getString("description")

            view.findViewById<TextView>(R.id.tutorial_text_title).text = title
            view.findViewById<ImageView>(R.id.tutorial_image).setImageResource(imageResId)
            view.findViewById<TextView>(R.id.tutorial_text).text = description
        }

        return view
    }
}