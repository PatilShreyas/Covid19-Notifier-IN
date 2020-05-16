package dev.shreyaspatil.covid19notify.utils

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

fun applyTheme(theme: Int) {
    AppCompatDelegate.setDefaultNightMode(theme)
}

/**
 * Returns if currently dark theme is active or not.
 */
fun AppCompatActivity.isDarkTheme(): Boolean {
    return (resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
}

/**
 * Returns [Boolean] based on current time.
 * Returns true if hours are between 06:00 pm - 07:00 am
 */
fun isNight(): Boolean {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return (currentHour <= 7 || currentHour >= 18)
}
