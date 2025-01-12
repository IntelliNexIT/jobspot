package com.intellinex.jobspot.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.intellinex.jobspot.R
import com.intellinex.jobspot.adapters.CareerTabAdapter

class CareerDetailActivity : AppCompatActivity() {

    private lateinit var careerViewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_career_detail)

        val careerId = intent.getStringExtra("career_id")

        val buttonBack: Button = findViewById(R.id.buttonBack)
        val buttonOption: Button = findViewById(R.id.buttonOption)

        buttonBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        // Tab Layout and Fragment Implementation
        careerViewPager = findViewById(R.id.careerViewPager)
        tabLayout = findViewById(R.id.careerTabLayout)

        val adapter = CareerTabAdapter(this)
        careerViewPager.adapter = adapter

        // Set up TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, careerViewPager) {
            tab, position ->
            when (position) {
                0 -> tab.text = "Overview"
                1 -> tab.text = "Requirement"
                2 -> tab.text = "About"
                else -> tab.text = "TAB ERROR"
            }

        }.attach()

    }
}