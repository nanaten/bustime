/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.util

import android.view.View
import com.nanaten.bustime.databinding.ToolbarBinding

fun ToolbarBinding.setToolbar(
    title: String,
    backVisibility: Int,
    settingVisibility: Int,
    backListener: ((View) -> Unit)? = null,
    settingListener: ((View) -> Unit)? = null
) {
    this.back.visibility = backVisibility
    this.back.setOnClickListener(backListener)
    this.setting.visibility = settingVisibility
    this.setting.setOnClickListener(settingListener)
    this.title.text = title
}