package com.intellinex.jobspot.ui.fragment

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.intellinex.jobspot.R
import com.intellinex.jobspot.databinding.FragmentRequirementBinding
import com.intellinex.jobspot.models.contents.CareerViewModel

class RequirementFragment : Fragment() {

    private var _binding: FragmentRequirementBinding? = null
    private val binding get() = _binding!!
    private val careerViewModel: CareerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequirementBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        careerViewModel.careerData.observe(viewLifecycleOwner) {data ->
            binding.textRequirement.text = Html.fromHtml(
                data.data.requirement?.trimIndent(),
                Html.FROM_HTML_OPTION_USE_CSS_COLORS
            )
        }

    }

}