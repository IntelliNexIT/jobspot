package com.intellinex.jobspot.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.intellinex.jobspot.R
import com.intellinex.jobspot.adapters.ImageCarouselAdapter
import com.intellinex.jobspot.adapters.RecentJobAdapter
import com.intellinex.jobspot.api.instances.HotInstance
import com.intellinex.jobspot.api.instances.RecentJobInstance
import com.intellinex.jobspot.api.resource.HotResponse
import com.intellinex.jobspot.api.resource.RecentJobResponse
import com.intellinex.jobspot.databinding.FragmentHomeBinding
import com.intellinex.jobspot.ui.auth.LoginActivity
import com.intellinex.jobspot.ui.screen.CareerDetailActivity
import com.intellinex.jobspot.utils.Authentication
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

    private lateinit var avatarImage: ShapeableImageView

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val view = inflater.inflate(R.layout.fragment_home,container, false)
//        viewPager = view.findViewById(R.id.carouselPromotion)
        _binding = FragmentHomeBinding.inflate(inflater, container, false) // Inflate the layout using binding
        viewPager = binding.carouselPromotion

        // Access the included header component's avatar ImageView
        avatarImage = binding.headerContainer.findViewById(R.id.avatar)

        sharedPreferences = requireActivity().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchHot()
        fetchRecentJob()
        setUpRecentJobAdapter()


        val auth = Authentication(sharedPreferences)


        avatarImage.setOnClickListener {

            if(auth.isAuthenticated()){
                val accountFragment = AccountFragment()

//                // Use FragmentManager to add or replace the fragment
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, accountFragment) // Replace with your container ID
                fragmentTransaction.addToBackStack(null) // Optional: adds the transaction to the back stack
                fragmentTransaction.commit()
            }else{
//                makeText(this.context, "You're not authenticated yet. Please Login.", LENGTH_LONG).show()
                "You have been authenticated. Please login to continue.".showDialog()
            }
        }

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
                    makeText(context, "Failed to load images", LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HotResponse>, t: Throwable) {
                makeText(context, "Error: ${t.message}", LENGTH_SHORT).show()
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
        RecentJobInstance.getApi(this).getRecentJob().enqueue(object: Callback<RecentJobResponse> {
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

    private fun String.showDialog() {
        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialong_alert, null)

        // Create the AlertDialog
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        // Create the dialog
        val dialog = dialogBuilder.create()

        val textMessage = dialogView.findViewById<MaterialTextView>(R.id.textMessage)
        val buttonCancel = dialogView.findViewById<MaterialButton>(R.id.cancelButton)
        val confirmButton = dialogView.findViewById<MaterialButton>(R.id.confirmButtom)

        textMessage.text = this

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        confirmButton.setOnClickListener {
            dialog.dismiss()

            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        dialog.show()
    }

}