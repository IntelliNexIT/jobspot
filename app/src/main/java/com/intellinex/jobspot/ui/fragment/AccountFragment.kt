package com.intellinex.jobspot.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.instances.AuthClient
import com.intellinex.jobspot.api.services.AuthService
import com.intellinex.jobspot.ui.screen.account.CompanyActivity
import com.intellinex.jobspot.ui.screen.account.EditProfileActivity
import com.intellinex.jobspot.ui.screen.account.LanguageActivity
import com.intellinex.jobspot.ui.screen.account.ResumeActivity
import com.intellinex.jobspot.utils.LoadingDialog
import com.intellinex.jobspot.utils.ToastMessage
import com.intellinex.jobspot.utils.UserManager
import kotlinx.coroutines.launch


class AccountFragment : Fragment() {

    private lateinit var cardViewLanguage: CardView
    private lateinit var cardViewResume: CardView
    private lateinit var cardViewCompany: CardView
    private lateinit var userAvatar: ShapeableImageView
    private lateinit var userNickname: MaterialTextView
    private lateinit var userLocation: MaterialTextView

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
        userAvatar = view.findViewById(R.id.userAvatar)
        userNickname = view.findViewById(R.id.userNickname)
        userLocation = view.findViewById(R.id.userLocation)
        cardViewLanguage = view.findViewById(R.id.cardViewLanguage)
        cardViewResume = view.findViewById(R.id.myResume)
        cardViewCompany = view.findViewById(R.id.myCompanyMaterialCardView)

        editProfileButton.setOnClickListener {
            val intent = Intent(this.context, EditProfileActivity::class.java)
            startActivity(intent)
        }

        cardViewLanguage.setOnClickListener {
            val intent = Intent(context, LanguageActivity::class.java)
            startActivity(intent)
        }
        cardViewResume.setOnClickListener {
            val intent = Intent(context, ResumeActivity::class.java)
            startActivity(intent)
        }
        cardViewCompany.setOnClickListener {
            val i = Intent(context, CompanyActivity::class.java)
            startActivity(i)
        }

        buttonLogout.setOnClickListener {
            onLogoutListener()
        }

        loadUserInformation()

    }

    private fun onLogoutListener() {
        val loadingDialog = LoadingDialog(this.context)
        loadingDialog.startLoadingDialog()

        val baseUrl = requireContext().getString(R.string.api_url)

        val retrofit = AuthClient.getAuthClient(requireContext(), baseUrl)
        val authService = retrofit.create(AuthService::class.java)

        lifecycleScope.launch {
            try {
                AuthClient.logout(requireContext(), authService)
                // Optionally navigate to the login screen or update UI
                // For example:
                // startActivity(Intent(requireContext(), LoginActivity::class.java))
                // finish() // Finish the current activity

                UserManager.clearUser()

                val toastMessage = ToastMessage(requireContext(), "Successfully logged out. Thank you!")
                toastMessage.startToast()
                toastMessage.setToastIcon(ContextCompat.getDrawable(requireContext(), R.drawable.success)!!)
                replaceFragment(HomeFragment())

            } catch (e: Exception) {
                // Handle any errors that occur during logout
                // For example, show a toast message
                val toastMessage = ToastMessage(requireContext(), "Logout failed! Please try again.")
                toastMessage.startToast()
                toastMessage.setToastIcon(ContextCompat.getDrawable(requireContext(), R.drawable.failed)!!)
            } finally {
                loadingDialog.dismissDialog() // Ensure the dialog is dismissed
            }
        }

    }

    private fun loadUserInformation() {
        val user = UserManager.user

        if(user != null) {
            context?.let { context ->
                Glide.with(context)
                    .load(user.avatar)
                    .error(R.drawable.user)
                    .placeholder(R.drawable.logo)
                    .into(userAvatar)
            }
            userNickname.text = user.nickname
            userLocation.text = user.location
        }

    }

    // Function to replace the current fragment with a new one
    private fun replaceFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.remove(fragment)
        transaction.replace(R.id.fragment_container, fragment) // Replace 'fragment_container' with your actual container ID
        transaction.addToBackStack(null) // Optional: add to back stack if you want to allow back navigation
        transaction.commit()
    }

}