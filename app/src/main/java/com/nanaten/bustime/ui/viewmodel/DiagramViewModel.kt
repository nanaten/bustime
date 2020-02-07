/*
 * Created by m.coder on 2020/1/31.
 * Copyright (c) 2020. nanaten All rights reserved.
 */

package com.nanaten.bustime.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanaten.bustime.network.entity.Calendar
import com.nanaten.bustime.network.entity.Diagram
import com.nanaten.bustime.network.usecase.DiagramUseCase
import com.nanaten.bustime.util.combine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DiagramViewModel @Inject constructor(private val useCase: DiagramUseCase) : ViewModel() {
    val calendar = MutableLiveData<Calendar>()
    val diagrams = MutableLiveData<List<Diagram>>()
    val todayZerotime = MutableLiveData<Long>(0L)
    val nowSecond = MutableLiveData<Long>(0L)
    val oldDate = MutableLiveData<String>()

    /**
     * 次のバスまでの時間を割り出す
     * todayZerotime: 本日の00時00分00秒時点のUnixTime
     * nowSecond: 現在の時間
     * diagrams: 本日の時刻表
     */
    val next: LiveData<Long> =
        combine(0L, todayZerotime, nowSecond, diagrams) { _, zeroTime, sec, diagrams ->
            val nearSecond = diagrams.firstOrNull { it.second >= (sec - zeroTime) }?.second ?: 0
            if (nearSecond != 0) nearSecond - (sec - zeroTime) else 0
        }
    val appIsActive = ObservableField<Boolean>(false)

    fun getCalendar() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        // 既にカレンダー取得済みならreturnする
        if (calendar.value != null && calendar.value?.date == today) {
            calendar.postValue(calendar.value)
            return
        }
        viewModelScope.launch {
            val cal = useCase.getTodayCalendar()
            cal.collect {
                calendar.postValue(it)
            }
        }
    }

    fun getDiagrams(target: String) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (oldDate.value == today) {
            diagrams.postValue(diagrams.value)
            return
        }
        viewModelScope.launch {
            try {
                val diagram = calendar.value?.diagram ?: return@launch
                val diagramName = "$diagram$target"
                val now = nowSecond.value?.minus(todayZerotime.value ?: 0L) ?: 0L
                val list = useCase.getDiagrams(diagramName, now)
                list.collect {
                    diagrams.postValue(it)
                }
                oldDate.postValue(today)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startTimer() {
        getZeroTime()
        if (appIsActive.get() == true) return
        appIsActive.set(true)
        viewModelScope.launch {
            while (appIsActive.get() == true) {
                val cal = java.util.Calendar.getInstance()
                val now = (cal.timeInMillis / 1000)
                nowSecond.postValue(now)
                delay(1000)
            }
        }
    }

    fun stopTimer() {
        if (appIsActive.get() != true) return
        appIsActive.set(false)
    }

    fun getZeroTime() {
        val cal = java.util.Calendar.getInstance()
        cal.set(
            cal.get(java.util.Calendar.YEAR),
            cal.get(java.util.Calendar.MONTH),
            cal.get(java.util.Calendar.DATE),
            0,
            0,
            0
        )
        val sec = (cal.timeInMillis / 1000)
        todayZerotime.postValue(sec)
    }
}