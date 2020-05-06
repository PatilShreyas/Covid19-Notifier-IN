package dev.shreyaspatil.covid19notify.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.widget.ListPopupWindow
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.shreyaspatil.covid19notify.R
import dev.shreyaspatil.covid19notify.utils.ThemeHelper

class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.dark_theme_preference, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        return when (preference?.key) {
            resources.getString(R.string.theme_pref_key) -> {
                themePopupMenu(listView.getChildAt(preference.order)).also { it.show() }
                true
            }
            else -> false
        }
    }

    private fun themePopupMenu(view: View?): ListPopupWindow {
        val keys = resources.getStringArray(R.array.theme_entry_array)
        val values = resources.getIntArray(R.array.theme_entry_array_values)
        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter<CharSequence>(requireContext(), R.layout.preference_theme_item, keys)

        return ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle).apply {
            setAdapter(adapter)
            anchorView = view
            setOnItemClickListener { parent, view, position, id ->
                this.dismiss()
                updateTheme(values[position])
            }
        }

    }

    private fun updateTheme(themeValue: Int) {
        preferenceManager.sharedPreferences.edit()
            .putInt(getString(R.string.theme_pref_key), themeValue).apply()
        ThemeHelper.applyTheme(themeValue)
        requireActivity().recreate()

    }

    companion object {
        const val TAG = "ThemeSettingsFragment"
    }
}