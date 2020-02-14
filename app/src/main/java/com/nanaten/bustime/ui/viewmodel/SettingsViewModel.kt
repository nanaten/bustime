/*
 * Created by m.coder on 2020/2/14.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nanaten.bustime.BuildConfig

class SettingsViewModel : ViewModel() {
    fun getVersion(): String {
        return BuildConfig.VERSION_NAME
    }
}