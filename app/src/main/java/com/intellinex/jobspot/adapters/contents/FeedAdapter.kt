package com.intellinex.jobspot.adapters.contents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.resource.FeedDataX
import com.intellinex.jobspot.databinding.CardFeedBinding
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class FeedAdapter(
    private val onFeedClick: (id: Int) -> Unit
): RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(val binding: CardFeedBinding): RecyclerView.ViewHolder(binding.root) {
        val viewPager: ViewPager2 = binding.feedImages
    }

    private val diffCallback = object : DiffUtil.ItemCallback<FeedDataX>() {
        override fun areItemsTheSame(oldItem: FeedDataX, newItem: FeedDataX): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FeedDataX, newItem: FeedDataX): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var feeds: List<FeedDataX>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(CardFeedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        val feed = feeds[position]

        holder.binding.apply {
            companyName.text = feed.company_name
            textDateTimeFromNow.text = formatTimeAgo(feed.created_at)
            Glide.with(holder.itemView.context)
                .load(feed.company_logo)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_radius)
                .into(companyProfile)
            feedDescription.text = feed.description
            reactionCount.text = when {
                feed.reaction_count != null -> {
                    feed.reaction_count.toString()
                }
                else -> "0"
            }

            if(feed.assets!!.isNotEmpty()) {
                layoutFeedImages.visibility = View.VISIBLE
                val adapter = ImageFeedViewPagerAdapter(feed.assets)
                feedImages.adapter = adapter

                feedImages.offscreenPageLimit = 1
            }else {
                layoutFeedImages.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener { onFeedClick(feed.id)}

    }

    private fun formatTimeAgo(createdAt: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val parsedDate = ZonedDateTime.parse(createdAt, formatter)
        val now = ZonedDateTime.now(ZoneId.of("Asia/Phnom_Penh"))
        val seconds = ChronoUnit.SECONDS.between(parsedDate, now)

        return when {
            seconds < 60 -> "$seconds seconds ago"
            seconds < 3600 -> "${seconds / 60} minutes ago"
            seconds < 86400 -> "${seconds / 3600} hours ago"
            else -> "${seconds / 86400} days ago"
        }
    }

    override fun getItemCount(): Int {
        return feeds.size
    }
}