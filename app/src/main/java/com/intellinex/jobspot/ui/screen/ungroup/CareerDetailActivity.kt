package com.intellinex.jobspot.ui.screen.ungroup

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textview.MaterialTextView
import com.intellinex.jobspot.R
import com.intellinex.jobspot.adapters.CareerTabAdapter
import com.intellinex.jobspot.api.instances.ContentServiceProvider
import com.intellinex.jobspot.api.resource.content.CareerDetailResponse
import com.intellinex.jobspot.api.services.CareerDetailRequest
import com.intellinex.jobspot.models.contents.CareerViewModel
import kotlinx.coroutines.launch

class CareerDetailActivity : AppCompatActivity() {

    private lateinit var careerViewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var buttonApply: MaterialButton
    private lateinit var title: MaterialTextView
    private lateinit var company_name: MaterialTextView
    private lateinit var location: MaterialTextView
    private lateinit var profile: ShapeableImageView
    private lateinit var employmentType: MaterialTextView
    private lateinit var adapter: CareerTabAdapter
    private val careerViewModel: CareerViewModel by viewModels()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_career_detail)

        val careerId = intent.getStringExtra("career_id")

        if (careerId != null) {
            getCareerDetail(careerId.toInt())
        }

        val buttonBack: Button = findViewById(R.id.buttonBack)
        val buttonOption: Button = findViewById(R.id.buttonOption)
        title = findViewById(R.id.careerTitle)
        company_name = findViewById(R.id.company_name)
        location = findViewById(R.id.company_location)
        profile = findViewById(R.id.companyProfile)
        employmentType = findViewById(R.id.employment_type)


        buttonBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        // Tab Layout and Fragment Implementation
        careerViewPager = findViewById(R.id.careerViewPager)
        tabLayout = findViewById(R.id.careerTabLayout)

        adapter = CareerTabAdapter(this)
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

    private fun getCareerDetail(id: Int) {
        val requestBody = CareerDetailRequest(id)

        lifecycleScope.launch {
            try {
                val client = ContentServiceProvider.getApi(this@CareerDetailActivity)
                val response = client.getCareerDetail(requestBody)
                Log.d("CAREER_DETAIL", "Successfully")
                UIUpdate(response)
            }catch (e: Exception) {
                Log.e("CAREER_DETAIL", e.message.toString())
            }
        }

    }

    private fun UIUpdate(career: CareerDetailResponse) {
        title.text = career.data.title
        company_name.text = career.data.company_name
        location.text = career.data.location_name

        Glide.with(this)
            .load(career.data.company_profile)
            .error(R.drawable.ic_launcher_background)
            .placeholder(R.drawable.logo)
            .into(profile)

        careerViewModel.careerData.value = career
    }
}