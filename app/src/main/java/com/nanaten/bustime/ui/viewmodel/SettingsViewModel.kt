/*
 * Created by m.coder on 2020/2/14.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nanaten.bustime.BuildConfig
import com.nanaten.bustime.SharedPref

class SettingsViewModel : ViewModel() {
    val isDarkMode = MutableLiveData<Boolean>()
    val firstView = MutableLiveData<Int>()

    fun getVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getIsDarkMode(context: Context): Boolean {
        val isDarkMode = SharedPref(context).getIsDarkMode()
        this.isDarkMode.postValue(isDarkMode)
        return isDarkMode
    }

    fun setIsDarkMode(context: Context) {
        val value = isDarkMode.value ?: false
        SharedPref(context).setIsDarkMode(value)
    }

    fun getFirstView(context: Context): Int {
        val firstView = SharedPref(context).getFirstViewSetting()
        this.firstView.postValue(firstView)
        return firstView
    }

    fun setFirstView(context: Context) {
        val value = firstView.value ?: 0
        SharedPref(context).setFirstViewSetting(value)
    }
}