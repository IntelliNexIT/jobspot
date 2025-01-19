package com.intellinex.jobspot.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.intellinex.jobspot.R
import com.intellinex.jobspot.databinding.FragmentAboutBinding
import com.intellinex.jobspot.models.contents.CareerViewModel

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private val careerViewModel: CareerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        careerViewModel.careerData.observe(viewLifecycleOwner) {data ->
            Glide.with(requireContext())
                .load(data.data.company_profile)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.logo)
                .into(binding.companyProfile)
            binding.apply {
                textCompanyName.text = data.data.company_name
                textIndustry.text = data.data.industry_name
                textCompanyAddress.text = data.data.location_name
                textFollowerCount.text = data.data.followers.toString()
            }
        }

    }


}