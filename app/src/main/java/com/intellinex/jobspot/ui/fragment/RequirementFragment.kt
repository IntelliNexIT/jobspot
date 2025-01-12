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
import com.intellinex.jobspot.R

class RequirementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requirement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textRequirement = view.findViewById<TextView>(R.id.textRequirement)
        val textFacilities = view.findViewById<TextView>(R.id.textFacilities)

        val html = "<ul>" +
                "<li> Experience 3+ years in graphic design.</li>" +
                "<li> Experience in Figma at least 5+ years." +
                "</ul>"

        textRequirement.text = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)

    }

}