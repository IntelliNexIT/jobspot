package com.intellinex.jobspot.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.textview.MaterialTextView
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.resource.content.CareerDetailResponse
import com.intellinex.jobspot.databinding.FragmentOverviewBinding
import com.intellinex.jobspot.models.contents.CareerViewModel


class OverviewFragment : Fragment() {

    private var careerData: CareerDetailResponse? = null
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    private val careerViewModel: CareerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        careerViewModel.careerData.observe(viewLifecycleOwner) {data ->
            binding.textJobDescription.text = data.data.description
            binding.textExperienceLevel.text = data.data.experience_level
            binding.textSalary.text = "$ ${data.data.salary.toFloat()}"

            // Populate skill list
            val skills = data.data.skills.split(",").map { it.trim() }
            binding.skillList.removeAllViews()
            for (skill in skills) {
            val skillTextView = MaterialTextView(requireContext()).apply {
                text = skill
                setPadding(8, 4, 8, 4)
                setBackgroundResource(R.drawable.ic_skill_background)
                setTextColor(resources.getColor(R.color.black, null))
                textSize = 12f
            }

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0,0,16,0)
                skillTextView.layoutParams = layoutParams

            binding.skillList.addView(skillTextView)
        }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}