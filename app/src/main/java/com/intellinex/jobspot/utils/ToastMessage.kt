package com.intellinex.jobspot.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.intellinex.jobspot.R

class ToastMessage(private var context: Context?, private var message: String?) {

    private lateinit var toastIcon: ShapeableImageView

    fun startToast() {
        val toastView = LayoutInflater.from(context).inflate(R.layout.toast_message, null)

        val toastMessage: MaterialTextView = toastView.findViewById(R.id.toastMessage)
        toastIcon= toastView.findViewById(R.id.toastIcon)

        toastIcon.setImageDrawable(ContextCompat.getDrawable(toastView.context, R.drawable.logo))
        toastMessage.text = message

        Toast(context).apply {
            duration=Toast.LENGTH_LONG
            setGravity(Gravity.BOTTOM, 0, 20)
            view=toastView
        }.show()
    }

    fun setToastIcon(icon: Drawable) {
        toastIcon.setImageDrawable(icon)
    }

}