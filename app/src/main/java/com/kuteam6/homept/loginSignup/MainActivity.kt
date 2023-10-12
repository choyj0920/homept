package com.kuteam6.homept.loginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.kuteam6.homept.databinding.ActivityMainBinding
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2500)
        installSplashScreen()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPagerTutorial : ViewPager2 = binding.viewPagerTutorial
        val dotsIndicator : DotsIndicator = binding.dotsIndicator

        val tutorialPageAdapter = TutorialPagerAdapter(this)

        viewPagerTutorial.adapter = tutorialPageAdapter

        dotsIndicator.setViewPager2(viewPagerTutorial)

        viewPagerTutorial.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        binding.btnLoginact.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
        }
    }
}