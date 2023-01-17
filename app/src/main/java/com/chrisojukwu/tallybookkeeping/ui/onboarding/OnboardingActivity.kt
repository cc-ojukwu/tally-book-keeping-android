package com.chrisojukwu.tallybookkeeping.ui.onboarding

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.ActivityOnboardingBinding
import com.chrisojukwu.tallybookkeeping.ui.account.SignInActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingActivityAdapter: OnboardingActivityAdapter
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onboardingActivityAdapter = OnboardingActivityAdapter(this)

        viewPager2 = binding.viewPagerContainer
        viewPager2.adapter = onboardingActivityAdapter

        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager2) { _, _ -> }.attach()

        val textViewNextButton: TextView = binding.textViewNextButton

        textViewNextButton.setOnClickListener {
            when(viewPager2.currentItem) {
                0 -> viewPager2.setCurrentItem(1, true)
                1 -> viewPager2.setCurrentItem(2, true)
                else -> {
                    startActivity(Intent(this@OnboardingActivity, SignInActivity::class.java))
                    finish()
                }
            }

        }

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position==2){
                    textViewNextButton.setText(R.string.finish_button)
                } else textViewNextButton.setText(R.string.next_button)
            }
        })


    }

    override fun onResume() {
        super.onResume()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.setDecorFitsSystemWindows(false)
//        } else {
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//        }
    }
}