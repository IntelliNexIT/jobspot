package com.intellinex.jobspot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intellinex.jobspot.api.resource.DataJobType
import com.intellinex.jobspot.databinding.JobtypeButtonBinding

class JobTypeAdapter(private val jobTypes: List<DataJobType>): RecyclerView.Adapter<JobTypeAdapter.JobTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobTypeViewHolder {
        val binding = JobtypeButtonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobTypeViewHolder, position: Int) {
        val jobTypeData = jobTypes[position]
        holder.bind(jobTypeData)
    }

    override fun getItemCount(): Int {
        return jobTypes.size
    }

    class JobTypeViewHolder(private val binding: JobtypeButtonBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(jobTypes: DataJobType){
            binding.jobTypeButtonCheck.text = jobTypes.title
            binding.jobTypeButtonCheck.isChecked = jobTypes.is_check
            binding.jobTypeButtonCheck.setOnCheckedChangeListener {_, isCheck ->
                jobTypes.is_check = isCheck
            }
        }
    }
}