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
        const val ALARM = "ALARM"
        const val ALARM_POSITION = "ALARM_POSITION"
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

    // アラーム設定時間
    fun getAlarmSec(): Int {
        return sharedPreferences.getInt(ALARM, 0)
    }

    fun setAlarmSec(position: Int, second: Int) {
        sharedPreferences.edit()
            .putInt(ALARM_POSITION, position)
            .apply()

        sharedPreferences.edit()
            .putInt(ALARM, second)
            .apply()
    }

    // アラームを設定したポジション
    fun getAlarmPos(): Int {
        return sharedPreferences.getInt(ALARM_POSITION, 0)
    }

    fun resetAlarm() {
        sharedPreferences.edit()
            .remove(ALARM_POSITION)
            .remove(ALARM)
            .apply()
    }
}