/*
 * Created by m.coder on 2020/2/14.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime

import android.content.Context

class SharedPref(context: Context) {
    companion object {
        const val SHARED_PREFERENCES_KEY = "BUSTIME_SHARED_PREFERENCES"
        const val FIRST_VIEW = "FIRST_VIEW"
        const val DARK_MODE = "DARK_MODE"
    }

    private val sharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)

    // 起動時に表示する項目
    fun getFirstViewSetting(): Int {
        return sharedPreferences.getInt(FIRST_VIEW, 0)
    }

    fun setFirstViewSetting(firstView: Int) {
        sharedPreferences.edit()
            .putInt(FIRST_VIEW, firstView)
            .apply()
    }

    // ダークモード ON/OFF
    fun getIsDarkMode(): Boolean {
        return sharedPreferences.getBoolean(DARK_MODE, false)
    }

    fun setIsDarkMode(isDarkMode: Boolean) {
        sharedPreferences.edit()
            .putBoolean(DARK_MODE, isDarkMode)
            .apply()
    }
}