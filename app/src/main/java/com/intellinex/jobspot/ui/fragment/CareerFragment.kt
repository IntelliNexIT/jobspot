package com.intellinex.jobspot.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.intellinex.jobspot.adapters.CareerAdapter
import com.intellinex.jobspot.api.instances.RetrofitClient
import retrofit2.Callback
import com.intellinex.jobspot.api.resource.ApiResponse
import com.intellinex.jobspot.databinding.FragmentCareerBinding
import com.intellinex.jobspot.ui.screen.CareerDetailActivity
import retrofit2.Call
import retrofit2.Response


class CareerFragment : Fragment() {

    private lateinit var careerAdapter: CareerAdapter
    private var _binding: FragmentCareerBinding? = null // Use a binding variable
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCareerBinding.inflate(inflater, container, false) // Inflate the layout using binding
        return binding.root // Return the root view of the binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCareers()
        setupRecyclerView() // Set up RecyclerView
    }

    private fun setupRecyclerView() {
        careerAdapter = CareerAdapter { career ->
            // Handle on click item
            val intent = Intent(context, CareerDetailActivity::class.java)
            intent.putExtra("career_id", career.id)
            startActivity(intent)
        }
        binding.recyclerCareer.apply { // Use the binding variable
            adapter = careerAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun fetchCareers() {

        RetrofitClient.instance.getCareers().enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val careers = response.body()?.data?.data // This returns the list of Job
                    careers?.let {
                        careerAdapter.careers = it // Update the adapter with the new list
                    }
                } else {
                    Log.e("Error", "Response error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("Error", "Network call failed: ${t.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference to avoid memory leaks
    }

}