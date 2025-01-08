package com.intellinex.jobspot.ui.screen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.intellinex.jobspot.R

class StarterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_starter)

        val nextButton: Button = findViewById(R.id.buttonContinue)

        nextButton.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        })

    }
}