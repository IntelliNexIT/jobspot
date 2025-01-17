package com.intellinex.jobspot.ui.screen.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.intellinex.jobspot.R
import java.util.Locale

class LanguageActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var radioEn : MaterialRadioButton
    private lateinit var radioCN: MaterialRadioButton
    private lateinit var radioKM: MaterialRadioButton

    private lateinit var cardViewEn: CardView
    private lateinit var cardViewKm: CardView
    private lateinit var cardViewCn: CardView

    private lateinit var buttonBack: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLanguage)) { v, insets ->
            val newInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, newInsets.top, 0, 0) // Adjust padding based on insets
            insets
        }

        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)

        radioEn = findViewById(R.id.radioEn)
        radioKM = findViewById(R.id.radioKM)
        radioCN = findViewById(R.id.radioCN)

        cardViewEn = findViewById(R.id.cardViewEn)
        cardViewKm = findViewById(R.id.cardViewKM)
        cardViewCn = findViewById(R.id.cardViewCN)
        buttonBack = findViewById(R.id.buttonBack)

        loadPreferenceLanguage()

        cardViewEn.setOnClickListener {
            if (!radioEn.isChecked) {
                radioEn.isChecked = true
                radioKM.isChecked = false
                radioCN.isChecked = false
                changeLanguage("en")
            }
        }

        cardViewKm.setOnClickListener {
            if (!radioKM.isChecked) {
                radioKM.isChecked = true
                radioEn.isChecked = false
                radioCN.isChecked = false
                changeLanguage("km")
            }
        }

        cardViewCn.setOnClickListener {
            if (!radioCN.isChecked) {
                radioCN.isChecked = true
                radioEn.isChecked = false
                radioKM.isChecked = false
                changeLanguage("zh")
            }
        }

        buttonBack.setOnClickListener {
            finish()
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
//
//        super.onBackPressed()
    }

    private fun changeLanguage(lang: String) {
        setLocale(lang)
        saveLanguagePrefs(lang)
        recreate()
    }

    private fun saveLanguagePrefs(lang: String) {
        with(sharedPreferences.edit()) {
            putString("ln", lang)
            apply()
        }
    }

    private fun loadPreferenceLanguage() {
        val ln = sharedPreferences.getString("ln", "en")
        setLocale(ln.toString())
        when (ln) {
            "en" -> radioEn.isChecked = true
            "km" -> radioKM.isChecked = true
            "zh" -> radioCN.isChecked =true
        }
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
}