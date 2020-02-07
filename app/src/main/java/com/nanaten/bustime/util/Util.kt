/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.util

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.distinctUntilChanged
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


inline fun <T : Any, LIVE1 : Any, LIVE2 : Any> combine(
    initialValue: T,
    liveData1: LiveData<LIVE1>,
    liveData2: LiveData<LIVE2>,
    crossinline block: (T, LIVE1, LIVE2) -> T
): LiveData<T> {
    return MediatorLiveData<T>().apply {
        value = initialValue
        listOf(liveData1, liveData2).forEach { liveData ->
            addSource(liveData) {
                val currentValue = value
                val liveData1Value = liveData1.value
                val liveData2Value = liveData2.value
                if (currentValue != null && liveData1Value != null && liveData2Value != null) {
                    value = block(currentValue, liveData1Value, liveData2Value)
                }
            }
        }
    }.distinctUntilChanged()
}


inline fun <T : Any, LIVE1 : Any, LIVE2 : Any, LIVE3 : Any> combine(
    initialValue: T,
    liveData1: LiveData<LIVE1>,
    liveData2: LiveData<LIVE2>,
    liveData3: LiveData<LIVE3>,
    crossinline block: (T, LIVE1, LIVE2, LIVE3) -> T
): LiveData<T> {
    return MediatorLiveData<T>().apply {
        value = initialValue
        listOf(liveData1, liveData2, liveData3).forEach { liveData ->
            addSource(liveData) {
                val currentValue = value
                val liveData1Value = liveData1.value
                val liveData2Value = liveData2.value
                val liveData3Value = liveData3.value
                if (currentValue != null && liveData1Value != null && liveData2Value != null && liveData3Value != null) {
                    value = block(currentValue, liveData1Value, liveData2Value, liveData3Value)
                }
            }
        }
    }.distinctUntilChanged()
}