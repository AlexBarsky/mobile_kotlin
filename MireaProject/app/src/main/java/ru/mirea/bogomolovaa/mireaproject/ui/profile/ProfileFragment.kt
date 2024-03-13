package ru.mirea.bogomolovaa.mireaproject.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import ru.mirea.bogomolovaa.mireaproject.R

class ProfileFragment :
    PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener
{
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        sharedPreferences = preferenceScreen.sharedPreferences!!
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val editor = sharedPreferences?.edit()
        val preference = key?.let { findPreference<Preference>(it) }
        preference?.let {
            when (it) {
                is EditTextPreference -> {
                    Toast.makeText(
                        requireContext(),
                        "EditTextPreference: ${it.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                    editor?.putString(key, it.text)?.apply()
                }
                is ListPreference -> {
                    Toast.makeText(
                        requireContext(),
                        "ListPreference: ${it.value}",
                        Toast.LENGTH_SHORT
                    ).show()
                    editor?.putString(key, it.value)?.apply()
                }
                is CheckBoxPreference -> {
                    Toast.makeText(
                        requireContext(),
                        "CheckBoxPreference: ${it.isChecked}",
                        Toast.LENGTH_SHORT
                    ).show()
                    editor?.putBoolean(key, it.isChecked)?.apply()
                }
                is SwitchPreferenceCompat -> {
                    Toast.makeText(
                        requireContext(),
                        "SwitchPreferenceCompat: ${it.isChecked}",
                        Toast.LENGTH_SHORT
                    ).show()
                    editor?.putBoolean(key, it.isChecked)?.apply()
                }
                else -> {}
            }
        }
    }
}
