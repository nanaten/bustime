/*
 * Created by m.coder on 2020/2/14.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nanaten.bustime.BuildConfig
import com.nanaten.bustime.SharedPref
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val sharedPref: SharedPref) : ViewModel() {
    val isDarkMode = MutableLiveData<Boolean>()
    val firstView = MutableLiveData<Int>()

    fun getVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getIsDarkMode() {
        val isDarkMode = sharedPref.getIsDarkMode()
        this.isDarkMode.postValue(isDarkMode)
    }

    fun setIsDarkMode() {
        val value = isDarkMode.value ?: false
        sharedPref.setIsDarkMode(value)
    }

    fun getFirstView(): Int {
        val firstView = sharedPref.getFirstViewSetting()
        this.firstView.postValue(firstView)
        return firstView
    }

    fun setFirstView(page: Int) {
        firstView.postValue(page)
        sharedPref.setFirstViewSetting(page)
    }
}