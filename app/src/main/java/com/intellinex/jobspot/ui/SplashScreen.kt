package com.intellinex.jobspot.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.intellinex.jobspot.ui.screen.StarterActivity
import com.intellinex.jobspot.R
import com.intellinex.jobspot.ui.screen.HomeActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val KEY_1: String = "isFirstRun"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE)

        Handler().postDelayed({

            val isFirstRun = sharedPreferences.getBoolean(KEY_1, true)

            if (isFirstRun){
                val intent: Intent = Intent(this, StarterActivity::class.java)
                startActivity(intent)

                with(sharedPreferences.edit()){
                    putBoolean(KEY_1, false)
                    apply()
                }
            }else{
                val intent: Intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }

            finish()
        }, 2500)
    }


}