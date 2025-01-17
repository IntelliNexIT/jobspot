package com.intellinex.jobspot.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.instances.AuthClient
import com.intellinex.jobspot.api.resource.UserResponse
import com.intellinex.jobspot.api.services.AuthService
import com.intellinex.jobspot.ui.auth.LoginActivity
import com.intellinex.jobspot.ui.fragment.AccountFragment
import com.intellinex.jobspot.ui.fragment.bottom.BottomSheetFragment
import com.intellinex.jobspot.ui.fragment.CareerFragment
import com.intellinex.jobspot.ui.fragment.FeedFragment
import com.intellinex.jobspot.ui.fragment.HomeFragment
import com.intellinex.jobspot.ui.screen.account.UnauthFragment
import com.intellinex.jobspot.utils.Authentication
import com.intellinex.jobspot.utils.InformationDialog
import com.intellinex.jobspot.utils.NetworkConnection
import com.intellinex.jobspot.utils.ToastMessage
import com.intellinex.jobspot.utils.UserManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fab: FloatingActionButton

    // Network Connection
    private lateinit var noConnectionLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)

        loadPreferenceLanguage()

        if(savedInstanceState == null){
            replaceFragment(HomeFragment(),"HomeFragment")
        }

        // Check Network Connection
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) {
            if(it) {
                showMainContent()
                fetchUserData(Authentication(sharedPreferences).isAuthenticated())
            }else{
                showNoConnectionLayout()
            }
        }


        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        fab = findViewById(R.id.buttonAdd)



        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_feed -> FeedFragment()
                R.id.nav_career -> CareerFragment()
                R.id.nav_account -> {
                    if(Authentication(sharedPreferences).isAuthenticated()) {
                        AccountFragment()
                    }else {
                        UnauthFragment()
                    }
                }
                else -> HomeFragment()
            }

            replaceFragment(selectedFragment, selectedFragment::class.java.simpleName)
            true
        }

//        val toastMessage = ToastMessage(this, "This is my testing toast.")
//        toastMessage.startToast()
//        toastMessage.setToastIcon(ContextCompat.getDrawable(this, R.drawable.success)!!)


        fab.setOnClickListener {
            if(Authentication(sharedPreferences).isAuthenticated()) {
                val bottomSheet = BottomSheetFragment()
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }else {
                val informationDialog = InformationDialog(this, "Unauthenticated", "You have not authenticated yet. Please log in to continue.")
                informationDialog.startInformationDialog()
                informationDialog.setPositiveButton {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                val warningIcon: Drawable = ContextCompat.getDrawable(this, R.drawable.warning)!!
                informationDialog.setAlertIcon(warningIcon)
            }
        }

        // Authenticate
//        if(auth.isAuthenticated()){
//            Toast.makeText(this, "You're authenticated", Toast.LENGTH_LONG).show()
//        }else {
//            Toast.makeText(this, "You're not authenticated", Toast.LENGTH_LONG).show()
//        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current fragment's tag
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment != null) {
            outState.putString("currentFragment", currentFragment::class.java.simpleName)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore the previous fragment on activity recreation
        val fragmentTag = savedInstanceState.getString("currentFragment")
        if (fragmentTag != null) {
            val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
            if (fragment != null) {
                replaceFragment(fragment, fragmentTag)
            }
        }
    }

    private fun fetchUserData(isAuthenticated: Boolean){
        val apiUrl = this.getString(R.string.api_url)
        if(isAuthenticated){
            val token = sharedPreferences.getString("JWT_TOKEN", null).toString()

            val authService = AuthClient.getAuthClient(apiUrl, token)
                .create(AuthService::class.java)

            val call: Call<UserResponse> = authService.user()

            call.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if(response.isSuccessful){
                        UserManager.user = response.body()?.data
                        val homeFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? HomeFragment
                        homeFragment?.loadUserInformation()
                        Log.d("USER_DATE", response.body()?.msg.toString())
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(this@HomeActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    @SuppressLint("CutPasteId")
    private fun showNoConnectionLayout() {
        if (!::noConnectionLayout.isInitialized) {
            noConnectionLayout = layoutInflater.inflate(R.layout.no_network_connection, null)
            // Add to the root layout
            val rootLayout = findViewById<FrameLayout>(R.id.fragment_container)
            rootLayout.removeAllViews()
            rootLayout.addView(noConnectionLayout)

            findViewById<FloatingActionButton>(R.id.buttonAdd).visibility = View.GONE

//            noConnectionLayout.findViewById<Button>(R.id.retryButton)?.setOnClickListener {
//                // Retry logic, for example, refresh the activity
//                recreate()
//            }
        }
        noConnectionLayout.visibility = View.VISIBLE
        findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE
        findViewById<View>(R.id.bottom_navigation).visibility = View.GONE
    }

    private fun showMainContent() {

        // If the no connection layout exists, hide it
        if (::noConnectionLayout.isInitialized) {
            noConnectionLayout.visibility = View.GONE
        }
        findViewById<View>(R.id.fragment_container).visibility = View.VISIBLE
        findViewById<View>(R.id.bottom_navigation).visibility = View.VISIBLE
        findViewById<FloatingActionButton>(R.id.buttonAdd).visibility = View.VISIBLE
    }

    private fun loadPreferenceLanguage() {
        val ln = sharedPreferences.getString("ln", "en")
        setLocale(ln.toString())
    }

    private fun setLocale(localeCode: String) {
        val newLocale = Locale(localeCode)
        Locale.setDefault(newLocale)

        val config = resources.configuration.apply {
            setLocale(newLocale)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.updateConfiguration(config, resources.displayMetrics)
        } else {
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {

        // Check if the fragment already exists
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)

        // Begin a fragment transaction
        val transaction = supportFragmentManager.beginTransaction()

        // Hide all other fragments
        for (frag in supportFragmentManager.fragments) {
            if (frag != null) {
                transaction.remove(frag)
            }
        }

        // If the fragment does not exist, add it
        if (existingFragment == null) {
            transaction.add(R.id.fragment_container, fragment, tag)
        } else {
            transaction.show(existingFragment)
        }

        transaction.commit()
    }
}