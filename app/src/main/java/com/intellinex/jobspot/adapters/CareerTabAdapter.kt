package com.intellinex.jobspot.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.intellinex.jobspot.ui.fragment.AboutFragment
import com.intellinex.jobspot.ui.fragment.OverviewFragment
import com.intellinex.jobspot.ui.fragment.RequirementFragment

class CareerTabAdapter(
    activity: FragmentActivity
): FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> OverviewFragment()
            1 -> RequirementFragment()
            2 -> AboutFragment()
            else -> Fragment()
        }
    }

    override fun getItemCount(): Int = 3
}