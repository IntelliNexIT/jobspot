package com.intellinex.jobspot.ui.fragment.bottom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.intellinex.jobspot.R
import com.intellinex.jobspot.adapters.IndustryFilterAdapter
import com.intellinex.jobspot.adapters.JobTypeAdapter
import com.intellinex.jobspot.api.instances.RetrofitClient
import com.intellinex.jobspot.api.resource.DataJobType
import com.intellinex.jobspot.api.resource.DataXIndustry
import com.intellinex.jobspot.api.resource.IndustryResponse
import com.intellinex.jobspot.api.resource.JobTypeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilterFragment : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewJobType: RecyclerView
    private lateinit var industryFilterAdapter: IndustryFilterAdapter
    private lateinit var jobTypeAdapter: JobTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Recycler View
        recyclerView = view.findViewById(R.id.recyclerViewIndustry)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewJobType = view.findViewById(R.id.recyclerViewJobType)
        recyclerViewJobType.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        fetchDataFromApi()
        fetchJobType()

    }

    private fun fetchDataFromApi() {
        RetrofitClient.getIndustryApi(this).getIndustries()
            .enqueue(object : Callback<IndustryResponse> {

                override fun onResponse(
                    call: Call<IndustryResponse>,
                    response: Response<IndustryResponse>
                ) {
                    if(response.isSuccessful){
                        // Assuming response.body()?.data is a list of industries
                        val industries = response.body()?.data?.data ?: emptyList()
                        // Map response to Item objects
                        val itemList = industries.map { industry ->
                            DataXIndustry(
                                industry.id,
                                industry.name,
                                industry.sub_industries,
                                industry.created_at,
                                industry.updated_at,
                                industry.is_checked
                            ) // Adjust this as per your industry model
                        }.toMutableList()
                        // Set the adapter with the new list
                        industryFilterAdapter = IndustryFilterAdapter(itemList)
                        recyclerView.adapter = industryFilterAdapter
                    }
                }

                override fun onFailure(call: Call<IndustryResponse>, t: Throwable) {
                    Log.e("INDUSTRY FETCH ERROR:", "${t.message}")
                }

            })
    }

    private fun fetchJobType() {
        RetrofitClient.getIndustryApi(this).getJobTypes().enqueue(object :
            Callback<JobTypeResponse> {
            override fun onResponse(
                call: Call<JobTypeResponse>,
                response: Response<JobTypeResponse>
            ) {
                if(response.isSuccessful){
                    val jobtypes = response.body()?.data ?: emptyList()
                    val item = jobtypes.map { jobtype ->
                        DataJobType(
                            jobtype.id,
                            jobtype.title,
                            jobtype.description,
                            jobtype.created_at,
                            jobtype.updated_at,
                            jobtype.is_check
                        )
                    }.toMutableList()
                    jobTypeAdapter = JobTypeAdapter(item)
                    recyclerViewJobType.adapter = jobTypeAdapter
                }
            }

            override fun onFailure(call: Call<JobTypeResponse>, t: Throwable) {
                Toast.makeText(context, "ERROR ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}