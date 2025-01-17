package com.intellinex.jobspot.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.intellinex.jobspot.R

class InformationDialog(
    private var context: Context?,
    private var title: String?,
    private var message: String?

) {
    private lateinit var dialog: Dialog
    private lateinit var positiveButton: MaterialButton
    private lateinit var negativeButton: MaterialButton
    private lateinit var alertIcon: ShapeableImageView

    fun startInformationDialog() {
        dialog = Dialog(context!!)

        val dialogView = LayoutInflater.from(context).inflate(R.layout.information_dialog, null)

        dialog.setContentView(dialogView)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogTitle: MaterialTextView = dialogView.findViewById(R.id.alertTitle)
        val dialogMessage: MaterialTextView = dialogView.findViewById(R.id.alertMessage)

        dialogTitle.text = title ?: "Information"
        dialogMessage.text = message ?: "This is the custom dialog"

        dialog.setCancelable(false)
        dialog.show()


        negativeButton = dialog.findViewById(R.id.buttonNegative)
        positiveButton = dialog.findViewById(R.id.buttonPositive)
        alertIcon = dialog.findViewById(R.id.alertIcon)

        negativeButton.setOnClickListener {
            dialog.dismiss()
        }

    }

    fun setPositiveButton (action: () -> Unit) {
        positiveButton.setOnClickListener {
            action()
            dialog.dismiss()
        }
    }

    fun setAlertIcon (icon: Drawable) {
        alertIcon.setImageDrawable(icon)
    }

    fun setPositiveText(text: String) {
        positiveButton.text = text
    }

    fun setNegativeText(text: String) {
        negativeButton.text = text
    }

}