package com.intellinex.jobspot.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.intellinex.jobspot.R
import com.intellinex.jobspot.ui.screen.account.EditProfileActivity
import com.intellinex.jobspot.ui.screen.account.LanguageActivity
import com.intellinex.jobspot.utils.LoadingDialog


class AccountFragment : Fragment() {

    private lateinit var cardViewLanguage: CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editProfileButton = view.findViewById<MaterialButton>(R.id.buttonEditProfile)
        val buttonLogout = view.findViewById<MaterialButton>(R.id.buttonLogout)
        cardViewLanguage = view.findViewById(R.id.cardViewLanguage)

        editProfileButton.setOnClickListener {
            val intent = Intent(this.context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        cardViewLanguage.setOnClickListener {
            val intent = Intent(context, LanguageActivity::class.java)
            startActivity(intent)
        }

        buttonLogout.setOnClickListener {
            onLogoutListener()
        }

    }

    private fun onLogoutListener() {
        val loadingDialog = LoadingDialog(this.context)
        loadingDialog.startLoadingDialog()
    }

}