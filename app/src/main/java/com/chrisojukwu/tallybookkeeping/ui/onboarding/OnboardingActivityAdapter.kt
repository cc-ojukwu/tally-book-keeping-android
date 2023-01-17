package com.chrisojukwu.tallybookkeeping.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingActivityAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    //Initialize list of fragments to be added to the viewPager
    private val fragmentList: List<Fragment> = listOf(
        OnboardingFirstFragment(),
        OnboardingSecondFragment(),
        OnboardingThirdFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}