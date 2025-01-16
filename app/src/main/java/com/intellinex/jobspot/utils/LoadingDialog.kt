package com.intellinex.jobspot.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.intellinex.jobspot.R

class LoadingDialog(private var activity: Context?) {

    private lateinit var dialog: Dialog

    fun startLoadingDialog() {
        dialog = Dialog(activity!!)

        // Inflate the custom layout
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.loading_dialog, null)
        dialog.setContentView(dialogView)

        // Set the background to be transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set dialog properties
        dialog.setCancelable(false)

        // Set fixed width (optional)
//        dialog.window?.setLayout(150, Dialog.LayoutParams.WRAP_CONTENT) // Adjust width as needed

        // Show the dialog
        dialog.show()
    }

    fun dismissDialog() {
        if (::dialog.isInitialized && dialog.isShowing) {
            dialog.dismiss()
        }
    }
}