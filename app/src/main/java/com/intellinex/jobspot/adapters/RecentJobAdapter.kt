package com.intellinex.jobspot.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intellinex.jobspot.R
import com.intellinex.jobspot.adapters.CareerAdapter.CareerViewHolder
import com.intellinex.jobspot.api.resource.Job
import com.intellinex.jobspot.databinding.CardCareerBinding
import com.intellinex.jobspot.databinding.HomeCardCareerBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class RecentJobAdapter(
    private val onItemClick: (Job) -> Unit
): RecyclerView.Adapter<RecentJobAdapter.CareerViewHolder>() {
    inner class CareerViewHolder(val binding: HomeCardCareerBinding) : RecyclerView.ViewHolder(binding.root)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.intellinex.jobspot.adapters.RecentJobAdapter.CareerViewHolder {
        return CareerViewHolder(HomeCardCareerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return careers.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: com.intellinex.jobspot.adapters.RecentJobAdapter.CareerViewHolder, position: Int) {
        val job = careers[position] // Get the job from the list


            holder.binding.apply {
                textCompanyName.text = job.company_name // Access company_name
                textPosition.text = job.title // Access title
                textAddress.text = job.location_name
                textSalary.text = job.salary
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
            // handle on item click
            holder.itemView.setOnClickListener {onItemClick(job)}

    }
}