package com.intellinex.jobspot.ui.screen.form

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.intellinex.jobspot.R

class NewFeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_feed)


        val buttonBack: Button = findViewById(R.id.buttonBack)

        buttonBack.setOnClickListener {
            finish()
        }

    }
}