package com.intellinex.jobspot.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.intellinex.jobspot.api.resource.Job
import com.intellinex.jobspot.databinding.CardCareerBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.bumptech.glide.Glide
import com.intellinex.jobspot.R

class CareerAdapter(
    private val onItemClick: (Job) -> Unit
) : RecyclerView.Adapter<CareerAdapter.CareerViewHolder>() {

    inner class CareerViewHolder(val binding: CardCareerBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem.id == newItem.id // Assuming 'id' is a unique identifier
        }

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var careers: List<Job>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareerViewHolder {
        return CareerViewHolder(CardCareerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return careers.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CareerViewHolder, position: Int) {
        val job = careers[position] // Get the job from the list

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Check if created_at is not null
        if (job.created_at != null) {
            // Parse the created_at date
            val createdAt = LocalDateTime.parse(job.created_at, formatter)

            // Get the current date and time
            val now = LocalDateTime.now()

            // Calculate the difference
            val seconds = ChronoUnit.SECONDS.between(createdAt, now)

            // Convert seconds to a human-readable format
            val timeAgo = when {
                seconds < 60 -> "$seconds seconds ago"
                seconds < 3600 -> "${seconds / 60} minutes ago"
                seconds < 86400 -> "${seconds / 3600} hours ago"
                else -> "${seconds / 86400} days ago"
            }

            holder.binding.apply {
                textCompanyName.text = job.company_name // Access company_name
                textPosition.text = job.title // Access title
                postDate.text = timeAgo // Set the formatted date
                textAddress.text = job.location_name
                textSalary.text = job.salary
                textDescription.text = job.description
                Glide.with(holder.itemView.context)
                    .load(job.company_profile)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(logoImage)
                // Assuming your Job model has a property for skills
                val skillsString = job.skills // This should be a comma-separated string

                // Clear previous skill views to avoid duplication
                holder.binding.skillList.removeAllViews()

                // Split the skills string into an array
                val skillsArray = skillsString.split(", ")

                // Loop through each skill and add to the skillList LinearLayout
                for (skill in skillsArray) {
                    val skillView = LayoutInflater.from(holder.itemView.context)
                        .inflate(R.layout.skill_material, holder.binding.skillList, false)

                    // Assuming skill_material has a TextView with id textSkill
                    val textSkill = skillView.findViewById<TextView>(R.id.textSkill)
                    textSkill.text = skill // Set the skill name

                    // Add the inflated view to the skillList LinearLayout
                    holder.binding.skillList.addView(skillView)
                }
            }

            // Handle On Item Click
            holder.itemView.setOnClickListener { onItemClick(job) }
        } else {
            holder.binding.postDate.text = "Date unavailable" // Handle null case
        }
    }
}