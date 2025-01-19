package com.intellinex.jobspot.adapters.contents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.intellinex.jobspot.R
import com.intellinex.jobspot.models.contents.ImageModel

class ImageAdapter(
    private val images: List<ImageModel>,
    private val onImageClick: (Int) -> Unit
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    class ImageViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ShapeableImageView = itemView.findViewById(R.id.shapeablImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.image_layout,
            parent,
            false
        )
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.imageView.setImageURI(images[position].uri)
        holder.imageView.setOnClickListener {
            onImageClick(position)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }
}