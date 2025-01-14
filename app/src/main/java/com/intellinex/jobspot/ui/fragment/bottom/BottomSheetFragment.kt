package com.intellinex.jobspot.ui.fragment.bottom

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.intellinex.jobspot.R
import com.intellinex.jobspot.ui.screen.form.CareerPostActivity
import com.intellinex.jobspot.ui.screen.form.NewFeedActivity

class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonNewFeed: Button = view.findViewById(R.id.buttonNewFeed)
        val buttonNewCareerPost: Button = view.findViewById(R.id.buttonNewCareerPost)

        buttonNewCareerPost.setOnClickListener {
            val intent = Intent(context, CareerPostActivity::class.java)
            startActivity(intent)
            dismiss()
        }
        buttonNewFeed.setOnClickListener {
            val intent = Intent(context, NewFeedActivity::class.java)
            startActivity(intent)
            dismiss()
        }
    }

}