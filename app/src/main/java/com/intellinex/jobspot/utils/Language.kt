package com.intellinex.jobspot.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import java.util.Locale

class Language {

    fun setLocale(context: Context, localeCode: String) {
        val newLocale = Locale(localeCode)
        Locale.setDefault(newLocale)

        val config = Resources.getSystem().configuration.apply {
            setLocale(newLocale)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            Resources.getSystem().updateConfiguration(config, Resources.getSystem().displayMetrics)
        }
    }

}