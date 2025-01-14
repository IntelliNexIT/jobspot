package com.intellinex.jobspot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.resource.DataCompany
import com.intellinex.jobspot.databinding.SelectAccountBinding

class SelectAccountAdapter(private val accountList: List<DataCompany>): RecyclerView.Adapter<SelectAccountAdapter.SelectAccountViewHolder>() {
    // Bind item
    inner class SelectAccountViewHolder(val binding: SelectAccountBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAccountViewHolder {
        return SelectAccountViewHolder(SelectAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: SelectAccountViewHolder, position: Int) {
        val account = accountList[position]

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(account.profile)
                .placeholder(R.drawable.logo)
                .error(R.drawable.ic_launcher_background)
                .into(companyProfile)
            companyIndustry.text = account.industry
        }
    }


    override fun getItemCount(): Int {
        return accountList.size
    }
}