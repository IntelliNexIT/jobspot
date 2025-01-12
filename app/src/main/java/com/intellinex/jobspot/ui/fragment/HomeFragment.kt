package com.intellinex.jobspot.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.intellinex.jobspot.adapters.ImageCarouselAdapter
import com.intellinex.jobspot.adapters.RecentJobAdapter
import com.intellinex.jobspot.api.instances.HotInstance
import com.intellinex.jobspot.api.instances.RecentJobInstance
import com.intellinex.jobspot.api.resource.HotResponse
import com.intellinex.jobspot.api.resource.RecentJobResponse
import com.intellinex.jobspot.databinding.FragmentHomeBinding
import com.intellinex.jobspot.ui.screen.CareerDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var imageCarouselAdapter: ImageCarouselAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val autoScrollDelay = 3000L // Delay in milliseconds

    private lateinit var recentJobAdapter: RecentJobAdapter
    private var _binding: FragmentHomeBinding? = null // Use a binding variable
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val view = inflater.inflate(R.layout.fragment_home,container, false)
//        viewPager = view.findViewById(R.id.carouselPromotion)
        _binding = FragmentHomeBinding.inflate(inflater, container, false) // Inflate the layout using binding
        viewPager = binding.carouselPromotion
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchHot()
        fetchRecentJob()
        setUpRecentJobAdapter()
    }


    private fun fetchHot(){
        HotInstance.getApi(this).getHots().enqueue(object : Callback<HotResponse>{
            override fun onResponse(call: Call<HotResponse>, response: Response<HotResponse>) {
                if (response.isSuccessful && response.body() != null){
                    val images = response.body()!!.data.map { it.image }
                    // Toast.makeText(context, "Successfully Fetched Promotion", Toast.LENGTH_SHORT).show()
                    imageCarouselAdapter = ImageCarouselAdapter(images)
                    viewPager.adapter = imageCarouselAdapter
                    startAutoScroll(images.size)
                }else{
                    Toast.makeText(context, "Failed to load images", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HotResponse>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startAutoScroll(size: Int) {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (size > 0) {
                    currentPage = (currentPage + 1) % size // Loop back to first item
                    viewPager.setCurrentItem(currentPage, true)
                }
                handler.postDelayed(this, autoScrollDelay)
            }
        }, autoScrollDelay)
    }

    private fun fetchRecentJob (){
        RecentJobInstance.instance.getRecentJob().enqueue(object: Callback<RecentJobResponse> {
            override fun onResponse(
                call: Call<RecentJobResponse>,
                response: Response<RecentJobResponse>
            ) {
                if(response.isSuccessful){
                    val recentJobs = response.body()?.data?.data
                    recentJobs?.let {
                        recentJobAdapter.careers = it
                    }
                }else{
                    Log.e("Error", "Response error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<RecentJobResponse>, t: Throwable) {
                Log.e("Error", "Network called failed: ${t.message}")
            }
        })
    }

    private fun setUpRecentJobAdapter(){
        recentJobAdapter = RecentJobAdapter {career ->
            val intent = Intent(context, CareerDetailActivity::class.java)
            intent.putExtra("career_id", career.id)
            startActivity(intent)
        }
        binding.recyclerRecentJob.apply {
            adapter = recentJobAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null) // Stop scrolling when the view is destroyed
    }

}