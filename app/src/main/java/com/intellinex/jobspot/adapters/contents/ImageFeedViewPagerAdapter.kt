package com.intellinex.jobspot.adapters.contents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intellinex.jobspot.R
import com.intellinex.jobspot.databinding.FeedImageLayoutBinding

class ImageFeedViewPagerAdapter(private val imageUrls: List<String>): RecyclerView.Adapter<ImageFeedViewPagerAdapter.ImageViewHolder>() {
    class ImageViewHolder(val binding: FeedImageLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val binding = FeedImageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ImageViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(imageUrls[position])
            .placeholder(R.drawable.logo)
            .error(R.drawable.ic_launcher_background)
            .into(holder.binding.feedImage)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }
}