/*
 * Created by m.coder on 2020/5/27.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.adapter

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("isVisible")
    fun isVisible(view: View, isVisible: Boolean?) {
        isVisible ?: return
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}