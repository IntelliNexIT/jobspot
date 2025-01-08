package com.intellinex.jobspot.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.intellinex.jobspot.ui.screen.StarterActivity
import com.intellinex.jobspot.R

class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val intent: Intent = Intent(this, StarterActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }


}