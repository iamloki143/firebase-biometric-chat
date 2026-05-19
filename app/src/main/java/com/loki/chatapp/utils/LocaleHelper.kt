package com.loki.chatapp.utils

import android.content.Context
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, lang: String): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}