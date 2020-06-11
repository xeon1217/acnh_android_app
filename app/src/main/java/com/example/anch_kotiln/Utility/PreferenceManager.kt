package com.example.anch_kotiln.Utility

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(val context: Context): Throwable() {
    private val PREFERENCES_NAME = "preference"

    private fun getPreferences() : SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, 0)
    }

    fun setValue(key: String, value: String) {
        val editor = getPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setVersion(key: String, value: Long) {
        val editor = getPreferences().edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun setBoolean(key: String, value: Boolean) {
        val editor = getPreferences().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getValue(key: String) : String? {
        return getPreferences().getString(key, "")
    }

    fun getVersion(key: String) : Long {
        return getPreferences().getLong(key, 0L)
    }

    fun getBoolean(key: String) : Boolean {
        return getPreferences().getBoolean(key, false)
    }

    fun removeValue(key: String) {
        var editor = getPreferences().edit()
        editor.remove(key)
        editor.apply()
    }
}

//https://re-build.tistory.com/37 참조