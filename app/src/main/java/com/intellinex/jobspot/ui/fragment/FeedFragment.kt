package com.intellinex.jobspot.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intellinex.jobspot.adapters.FeedAdapter
import com.intellinex.jobspot.api.instances.RetrofitClient
import com.intellinex.jobspot.api.resource.FeedResponse
import com.intellinex.jobspot.databinding.FragmentFeedBinding
import com.intellinex.jobspot.ui.screen.ungroup.FeedDetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment : Fragment() {

    // 1. Global Variables Initialization
    private lateinit var feedAdapter: FeedAdapter
    private var _binding: FragmentFeedBinding? = null
    private val binding get() =_binding!!
    private var currentPage = 1
    private var isLoading = false
    private var isLastPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 2. Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    // 3. Created View Execute
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchFeed(currentPage)
        setUpFeedRecyclerView()
        
        binding.feedRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.feedRecyclerView.layoutManager as LinearLayoutManager
                if(!isLoading && !isLastPage && layoutManager.findLastCompletelyVisibleItemPosition() == feedAdapter.itemCount -1){
                    currentPage++
                    fetchFeed(currentPage)
                }
            }
        })
    }

    // API handler
    private fun fetchFeed(page: Int) {
        isLoading = true
        RetrofitClient.getFeedsApi(this).getFeeds(page)
            .enqueue(object : Callback<FeedResponse> {
                override fun onResponse(
                    call: Call<FeedResponse>,
                    response: Response<FeedResponse>
                ) {
                    isLoading = false
                    if(response.isSuccessful) {
                        Log.d("FEED SUCCESS", response.body()?.msg.toString())
                        val feeds = response.body()?.data?.data
                        feeds?.let {
                            feedAdapter.feeds = it
                        }
                        isLastPage = currentPage >= (response.body()?.data?.last_page ?: 0)
                    }else {
                        Log.e("FEED ERROR", "Response error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<FeedResponse>, t: Throwable) {
                    Log.e("FEED FAILED ", "Network call failed: ${t.message}")
                }
            })
    }

    // Set up recycler view
    private fun setUpFeedRecyclerView() {
        // On item clicked

        val dividerItemDecoration = DividerItemDecoration(binding.feedRecyclerView.context, LinearLayoutManager.VERTICAL)
        binding.feedRecyclerView.addItemDecoration(dividerItemDecoration)

        feedAdapter = FeedAdapter { id ->
            val intent = Intent(context, FeedDetailActivity::class.java)
            intent.putExtra("FEED_ID", id.toString())
            startActivity(intent)
        }

        binding.feedRecyclerView.apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    // Reduce the performance after this view close
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}