package com.intellinex.jobspot.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intellinex.jobspot.api.resource.DataXIndustry
import com.intellinex.jobspot.databinding.IndustryCheckboxBinding

class IndustryFilterAdapter(private val industryList: List<DataXIndustry>): RecyclerView.Adapter<IndustryFilterAdapter.IndustryViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndustryViewHolder {
       val binding = IndustryCheckboxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = industryList[position]
        holder.bind(industry)
    }

    override fun getItemCount(): Int {
        return industryList.size
    }


    class IndustryViewHolder(private val binding: IndustryCheckboxBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(industry: DataXIndustry) {
            binding.industryCheckBox.text = industry.name
            binding.industryCheckBox.isChecked = industry.is_checked
            binding.industryCheckBox.setOnCheckedChangeListener { _, isChecked ->
                industry.is_checked = isChecked
            }
        }
    }
}