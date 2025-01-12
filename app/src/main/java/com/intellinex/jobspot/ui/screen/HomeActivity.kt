package com.intellinex.jobspot.ui.screen

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intellinex.jobspot.R
import com.intellinex.jobspot.databinding.ActivityHomeBinding
import com.intellinex.jobspot.ui.fragment.AccountFragment
import com.intellinex.jobspot.ui.fragment.BookmarkFragment
import com.intellinex.jobspot.ui.fragment.BottomSheetFragment
import com.intellinex.jobspot.ui.fragment.CareerFragment
import com.intellinex.jobspot.ui.fragment.ConnectionFragment
import com.intellinex.jobspot.ui.fragment.FeedFragment
import com.intellinex.jobspot.ui.fragment.HomeFragment

class HomeActivity : AppCompatActivity() {
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

    }
}