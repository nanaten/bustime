/*
 * Created by m.coder on 2020/2/14.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nanaten.bustime.BuildConfig
import com.nanaten.bustime.R
import com.nanaten.bustime.network.usecase.SettingsUseCase
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val useCase: SettingsUseCase) :
    ViewModel() {
    val isDarkMode = MutableLiveData<Boolean>()
    val firstView = MutableLiveData<Int>()
    val toolbarTitle = MutableLiveData<Int>(R.string.setting)

    fun getVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getDarkModeIsOn(): Boolean {
        val isDarkMode = useCase.getIsDarkMode()
        this.isDarkMode.postValue(isDarkMode)
        return isDarkMode
    }

    fun setDarkModeStatus() {
        val value = isDarkMode.value ?: false
        useCase.setIsDarkMode(value)
    }

    fun getFirstViewSetting(): Int {
        val firstView = useCase.getFirstViewSetting()
        this.firstView.postValue(firstView)
        return firstView
    }

    fun setFirstView(page: Int) {
        firstView.postValue(page)
        useCase.setFirstViewSetting(page)
    }
}