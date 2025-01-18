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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
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
import com.intellinex.jobspot.ui.fragment.bottom.FilterFragment
import com.intellinex.jobspot.ui.screen.ungroup.CareerDetailActivity
import com.intellinex.jobspot.ui.screen.ungroup.NotificationActivity
import com.intellinex.jobspot.utils.Authentication
import com.intellinex.jobspot.utils.InformationDialog
import com.intellinex.jobspot.utils.UserManager
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
    private lateinit var nickname: MaterialTextView

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
        nickname = binding.nickname

        sharedPreferences = requireActivity().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchHot()
        fetchRecentJob()
        setUpRecentJobAdapter()
        loadUserInformation()

        val auth = Authentication(sharedPreferences)

        avatarImage.setOnClickListener {

            if(auth.isAuthenticated()){
                val accountFragment = AccountFragment()

//                // Use FragmentManager to add or replace the fragment
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_container, accountFragment) // Replace with your container ID
                fragmentTransaction.addToBackStack(null) // Optional: adds the transaction to the back stack
                fragmentTransaction.commit()

                val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNavigationView.selectedItemId = R.id.nav_account

            }else{
                val informationDialog = InformationDialog(this.requireContext(), "Unauthenticated", "You have not been authenticated yet. Please sign in to continue.")
                informationDialog.startInformationDialog()
                informationDialog.setNegativeText("No")
                informationDialog.setAlertIcon(ContextCompat.getDrawable(this.requireContext(),R.drawable.warning)!!)
            }
        }

        val notificationButton = view.findViewById<MaterialButton>(R.id.button_notification)
        notificationButton.setOnClickListener {
            val intent = Intent(this.context, NotificationActivity::class.java)
            startActivity(intent)
        }

        val buttonFilter = view.findViewById<MaterialButton>(R.id.buttonFilter)
        buttonFilter.setOnClickListener {
            val filterFragment = FilterFragment()
            filterFragment.show(parentFragmentManager, filterFragment.tag)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUserInformation()
    }
    fun loadUserInformation() {
        val user = UserManager.user
        if (user != null) {
                context?.let { context ->
                    Glide.with(context)
                        .load(user.avatar)
                        .placeholder(R.drawable.logo) // Add a placeholder image
                        .error(R.drawable.ic_radius) // Add an error image
                        .into(avatarImage)
                }
                nickname.text = user.nickname
                Log.d("HOME-FRAGMENT", "User information loaded: ${user.nickname}")

        } else {
            // Optionally handle the case where user information is not available
            Log.e("HOME-FRAGMENT", "User information is not available.")
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
                    Log.e("HOME_FRAGMENT", "Failed to load images")
                }
            }

            override fun onFailure(call: Call<HotResponse>, t: Throwable) {
               Log.e("HOME_FREGMENT", t.message.toString())
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

//    private fun String.showDialog() {
//        // Inflate the custom dialog layout
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialong_alert, null)
//
//        // Create the AlertDialog
//        val dialogBuilder = AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .setCancelable(true)
//
//        // Create the dialog
//        val dialog = dialogBuilder.create()
//
//        val textMessage = dialogView.findViewById<MaterialTextView>(R.id.textMessage)
//        val buttonCancel = dialogView.findViewById<MaterialButton>(R.id.cancelButton)
//        val confirmButton = dialogView.findViewById<MaterialButton>(R.id.confirmButtom)
//
//        textMessage.text = this
//
//        buttonCancel.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        confirmButton.setOnClickListener {
//            dialog.dismiss()
//
//            val intent = Intent(context, LoginActivity::class.java)
//            startActivity(intent)
//        }
//
//        dialog.show()
//    }

}