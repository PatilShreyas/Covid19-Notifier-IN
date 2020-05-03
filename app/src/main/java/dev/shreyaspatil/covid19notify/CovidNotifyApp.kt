package dev.shreyaspatil.covid19notify

import android.app.Application
import androidx.preference.PreferenceManager
import dev.shreyaspatil.covid19notify.di.networkModule
import dev.shreyaspatil.covid19notify.di.viewModelModule
import dev.shreyaspatil.covid19notify.utils.ThemeHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class CovidNotifyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTheme()
    }

    private fun initTheme() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        ThemeHelper.applyTheme(
            preferences.getInt(getString(R.string.theme_pref_key), ThemeHelper.DEFAULT)
        )
    }

    private fun initKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(networkModule, viewModelModule)
        }
    }
}