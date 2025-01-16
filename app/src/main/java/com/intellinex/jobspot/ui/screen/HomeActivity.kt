package com.intellinex.jobspot.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.instances.AuthClient
import com.intellinex.jobspot.api.resource.UserResponse
import com.intellinex.jobspot.api.services.AuthService
import com.intellinex.jobspot.ui.fragment.AccountFragment
import com.intellinex.jobspot.ui.fragment.BookmarkFragment
import com.intellinex.jobspot.ui.fragment.bottom.BottomSheetFragment
import com.intellinex.jobspot.ui.fragment.CareerFragment
import com.intellinex.jobspot.ui.fragment.FeedFragment
import com.intellinex.jobspot.ui.fragment.HomeFragment
import com.intellinex.jobspot.utils.Authentication
import com.intellinex.jobspot.utils.NetworkConnection
import com.intellinex.jobspot.utils.UserManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var fab: FloatingActionButton

    // Network Connection
    private lateinit var noConnectionLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)

        // Check Network Connection
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this) {
            if(it) {
                showMainContent()
                fetchUserData(Authentication(sharedPreferences).isAuthenticated())

                if(savedInstanceState == null){
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                }

            }else{
                showNoConnectionLayout()
            }
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        fab = findViewById(R.id.buttonAdd)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val selectedFragment: Fragment = when(item.itemId) {
                R.id.nav_feed -> FeedFragment()
                R.id.nav_career -> CareerFragment()
                R.id.nav_bookmark -> BookmarkFragment()
                R.id.nav_account -> AccountFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()
            true
        }


        fab.setOnClickListener {
            val bottomSheet = BottomSheetFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        // Authenticate
//        if(auth.isAuthenticated()){
//            Toast.makeText(this, "You're authenticated", Toast.LENGTH_LONG).show()
//        }else {
//            Toast.makeText(this, "You're not authenticated", Toast.LENGTH_LONG).show()
//        }

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
}