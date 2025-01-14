package com.intellinex.jobspot.ui.screen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.intellinex.jobspot.R
import com.intellinex.jobspot.ui.fragment.AccountFragment
import com.intellinex.jobspot.ui.fragment.BookmarkFragment
import com.intellinex.jobspot.ui.fragment.bottom.BottomSheetFragment
import com.intellinex.jobspot.ui.fragment.CareerFragment
import com.intellinex.jobspot.ui.fragment.ConnectionFragment
import com.intellinex.jobspot.ui.fragment.HomeFragment
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val fab = findViewById<FloatingActionButton>(R.id.buttonAdd)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val selectedFragment: Fragment = when(item.itemId) {
                R.id.nav_network -> ConnectionFragment()
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
        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)

        if(isAuthenticated()){
            Toast.makeText(this, "You're authenticated", Toast.LENGTH_LONG).show()
        }else {
            Toast.makeText(this, "You're not authenticated", Toast.LENGTH_LONG).show()
        }

    }

    private fun isAuthenticated(): Boolean {
        val token = sharedPreferences.getString("JWT_TOKEN", null)
        return token != null && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        // Optionally decode the JWT to check its expiration
        // Example to decode JWT (you can use a library like jwt_decoder)
        val parts = token.split(".")
        if (parts.size != 3) return true // Not a valid JWT

        // Decode the payload (the second part)
        val payload = String(Base64.decode(parts[1], Base64.DEFAULT))
        val jsonObject = JSONObject(payload)
        val exp = jsonObject.getLong("exp") // Get expiration time

        // Check if the current time is past the expiration time
        return exp < System.currentTimeMillis() / 1000
    }
}